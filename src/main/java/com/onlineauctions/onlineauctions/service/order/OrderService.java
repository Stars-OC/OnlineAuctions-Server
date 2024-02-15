package com.onlineauctions.onlineauctions.service.order;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.onlineauctions.onlineauctions.mapper.OrderInfoMapper;
import com.onlineauctions.onlineauctions.mapper.OrderMapper;
import com.onlineauctions.onlineauctions.pojo.PageList;
import com.onlineauctions.onlineauctions.pojo.PaidInfo;
import com.onlineauctions.onlineauctions.pojo.WalletInfo;
import com.onlineauctions.onlineauctions.pojo.auction.Auction;
import com.onlineauctions.onlineauctions.pojo.auction.Cargo;
import com.onlineauctions.onlineauctions.pojo.type.AuctionStatus;
import com.onlineauctions.onlineauctions.pojo.type.OrderStatus;
import com.onlineauctions.onlineauctions.pojo.user.balance.Order;
import com.onlineauctions.onlineauctions.pojo.user.balance.OrderInfo;
import com.onlineauctions.onlineauctions.pojo.user.balance.Wallet;
import com.onlineauctions.onlineauctions.service.CargoService;
import com.onlineauctions.onlineauctions.service.auction.AuctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderInfoMapper orderInfoMapper;

    private final OrderMapper orderMapper;

    private final BalanceService balanceService;

    private final RabbitMQService rabbitMQService;

    private final AuctionService auctionService;

    private final CargoService cargoService;

    /**
     * 查询订单信息
     *
     * @param username 用户名
     * @param orderId 订单ID
     * @return 订单信息对象
     */
    public OrderInfo orderInfo(long username, long orderId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username).eq("order_id", orderId);
        Order order = orderMapper.selectOne(queryWrapper);
        if (order == null) return null;
        return orderInfoMapper.selectById(orderId);
    }

    /**
     * 查询订单列表
     *
     * @param username 用户名
     * @param pageNum 当前页码
     * @param pageSize 每页数量
     * @return 分页列表对象
     */
    public PageList<OrderInfo> orderList(long username, int pageNum, int pageSize) {
        // 获取订单信息列表
        List<OrderInfo> orderInfoList = orderMapper.getOrderInfoList(username, pageNum - 1, pageSize);
        // 创建分页列表对象
        PageList<OrderInfo> pageList = new PageList<>();
        // 创建查询条件
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        // 查询订单总数
        Long selectCount = orderMapper.selectCount(queryWrapper);
        // 设置分页列表的总数量和数据
        pageList.setCount(selectCount);
        pageList.setData(orderInfoList);
        return pageList;
    }


    public OrderInfo getOrderInfo(String orderId) {
        return orderInfoMapper.selectById(orderId);
    }

    /**
     * 超时自动取消订单
     *
     * @param orderId 订单ID
     */
    public void cancelOrder(String orderId) {
        // 根据订单ID查询订单
        Order order = orderMapper.selectById(orderId);
        // 如果订单不存在
        if (order != null){
            // 调用cancelOrderByUser方法取消订单
            Wallet wallet = cancelOrderByUser(order.getUsername(), orderId);
            if (wallet != null) {
                // 更新订单状态
                QueryWrapper<OrderInfo> orderInfoQueryWrapper = new QueryWrapper<>();
                orderInfoQueryWrapper.eq("order_id", orderId);
                orderInfoMapper.update(OrderInfo.builder().status(OrderStatus.PAID_TIMEOUT.getStatus()).build(), orderInfoQueryWrapper);
            }
        }
    }


    /**
     * 取消订单
     *
     * @param username 用户名
     * @param orderId 订单ID
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Wallet cancelOrderByUser(long username, String orderId) {
        // 创建查询条件
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username).eq("order_id", orderId);

        // 根据查询条件查询订单
        Order order = orderMapper.selectOne(queryWrapper);

        // 如果订单存在
        if (order != null) {
            // 根据订单ID查询订单信息
            OrderInfo orderInfo = orderInfoMapper.selectById(orderId);

            // 防止取消正常订单
            if (orderInfo.getStatus() != OrderStatus.PAYING.getStatus()) return null;

            // 将订单状态设置为已取消
            orderInfo.setStatus(OrderStatus.CANCELED.getStatus());

            // 更新订单信息
            orderInfoMapper.updateById(orderInfo);

            // 更新货物状态为未售出
            auctionService.unsoldAuction(orderInfo.getCargoId());

            // 取消订单并支付
            return balanceService.cancelOrderPaid(order.getUsername(), orderInfo.getBalance());
        }

        return null;
    }

    /**
     * 通过拍卖创建订单信息
     *
     * @param username 用户名
     * @param auctionId 拍卖ID
     * @return 订单信息
     */
    @Transactional()
    public OrderInfo createOrderInfoByAuction(long username, long auctionId) {
        // 获取拍卖信息
        Auction auction = auctionService.getAuctionInfoByAuctionId(auctionId);
        Long cargoId = auction.getCargoId();
        // 获取货物信息
        Cargo cargo = cargoService.cargoInfo(cargoId);
        // 如果拍卖状态不是已售出，则返回null
        if (auction.getStatus() != AuctionStatus.SOLD.getStatus()) return null;
        // 构建订单信息
        OrderInfo orderInfo = OrderInfo.builder()
                .cargoId(cargoId)
                .title(cargo.getName())
                .description(cargo.getDescription())
                .balance(auction.getHammerPrice())
                .build();
        // 插入订单信息到数据库
        orderInfoMapper.insert(orderInfo);
        // 构建订单对象
        Order order = Order.builder().orderId(orderInfo.getOrderId())
                .username(username)
                .build();
        // 插入订单到数据库
        orderMapper.insert(order);
        // 将订单信息延迟发送到消息队列
        rabbitMQService.delayOrder(orderInfo.getOrderId());
        return orderInfo;
    }

    /**
     * 事务性方法，用于支付订单
     *
     * @param username 用户名
     * @param orderId 订单ID
     * @return 订单信息
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PaidInfo payOrder(long username, long orderId , String password) {

        // 根据订单ID查询订单信息
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);

        //判断是否支付过
        QueryWrapper<Order> queryOrderWrapper = new QueryWrapper<>();
        queryOrderWrapper.eq("order_id", orderId);
        Order order = orderMapper.selectOne(queryOrderWrapper);
        if (order != null) return PaidInfo.builder().success(false).message("已支付，不要再支付了").build();

        // 如果订单存在
        if (orderInfo != null) {
            // 获取订单余额
            BigDecimal balance = orderInfo.getBalance();

            // 根据用户ID支付余额
            WalletInfo walletInfo = balanceService.payByUser(username, balance , password);

            // 如果支付成功
            if (walletInfo != null) {
                if (walletInfo.isSuccess()){

                    orderInfo.setStatus(OrderStatus.PAID.getStatus());

                    orderInfoMapper.updateById(orderInfo);

                    // 发送已支付订单消息到RabbitMQ
                    rabbitMQService.paidOrder(orderInfo.getOrderId());

                    // 创建订单日志 这个如果有别的id重复提交会报错
                    createOrderLog(username, orderId);

                    return PaidInfo.builder().success(true).message(walletInfo.getMessage()).orderInfo(orderInfo).build();
                }else {
                    return PaidInfo.builder().success(false).message(walletInfo.getMessage()).build();
                }
            }
            return PaidInfo.builder().success(false).message("支付失败，密码错误").build();
        }

        return PaidInfo.builder().success(false).message("支付失败，订单不存在").build();
    }

    /**
     * 创建订单日志
     *
     * @param username 用户名
     * @param orderId 订单ID
     */
    public void createOrderLog(long username, long orderId){
        orderMapper.insert(Order.builder().username(username).orderId(orderId).build());
    }

}

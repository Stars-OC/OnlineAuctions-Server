package com.onlineauctions.onlineauctions.service.order;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onlineauctions.onlineauctions.mapper.CargoMapper;
import com.onlineauctions.onlineauctions.mapper.OrderInfoMapper;
import com.onlineauctions.onlineauctions.mapper.OrderMapper;
import com.onlineauctions.onlineauctions.mapper.WalletMapper;
import com.onlineauctions.onlineauctions.pojo.PageList;
import com.onlineauctions.onlineauctions.pojo.auction.Cargo;
import com.onlineauctions.onlineauctions.pojo.type.CargoStatus;
import com.onlineauctions.onlineauctions.pojo.type.OrderStatus;
import com.onlineauctions.onlineauctions.pojo.user.balance.Order;
import com.onlineauctions.onlineauctions.pojo.user.balance.OrderInfo;
import com.onlineauctions.onlineauctions.pojo.user.balance.Wallet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderInfoMapper orderInfoMapper;

    private final OrderMapper orderMapper;

    private final CargoMapper cargoMapper;

    private final WalletMapper walletMapper;

    private final BalanceService balanceService;

    private final RabbitMQService rabbitMQService;

    /**
     * 查询订单信息
     *
     * @param username 用户名
     * @param orderId 订单ID
     * @return 订单信息对象
     */
    public OrderInfo orderInfo(long username, long orderId) {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username).eq("orderId", orderId);
        return orderInfoMapper.selectOne(queryWrapper);
    }

    /**
     * 查询订单列表
     *
     * @param username 用户名
     * @param pageNum 当前页码
     * @param pageSize 每页数量
     * @param filter 过滤条件
     * @return 分页列表对象
     */
    public PageList<Order> orderList(long username, int pageNum, int pageSize, String filter) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(filter)) queryWrapper.like("title", filter);
        queryWrapper.eq("username", username).orderByDesc("create_at");
        Page<Order> page = new Page<>(pageNum, pageSize);
        return new PageList<>(orderMapper.selectPage(page,queryWrapper));
    }

    public OrderInfo createOrderInfo(OrderInfo order) {
        orderInfoMapper.insert(order);
        return order;
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
        // 创建查询条件
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        // 根据订单ID查询订单
        Order order = orderMapper.selectById(queryWrapper);
        // 如果订单存在
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
        Order order = orderMapper.selectById(queryWrapper);

        // 如果订单存在
        if (order != null) {
            // 根据订单ID查询订单信息
            OrderInfo orderInfo = orderInfoMapper.selectById(orderId);

            // 将订单状态设置为已取消
            orderInfo.setStatus(OrderStatus.CANCELED.getStatus());

            // 更新订单信息
            orderInfoMapper.updateById(orderInfo);

            // 更新货物状态为未售出
            cargoMapper.updateCargoStatus(orderInfo.getCargoId(), CargoStatus.UNSOLD.getStatus());

            // 取消订单并支付
            return balanceService.cancelOrderPaid(order.getUsername(), orderInfo.getBalance());
        }

        return null;
    }

//    public OrderInfo createOrderInfo(long username, long cargoId) {
//        QueryWrapper<Cargo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("cargo_id", cargoId);
//        Cargo cargo = cargoMapper.selectOne(queryWrapper);
//        if (cargo != null) {
//            OrderInfo orderInfo = OrderInfo.builder()
//                    .title(cargo.getName())
//                    .description(cargo.getDescription())
//                    .balance(cargo)
//                    .status(OrderStatus.PENDING.getStatus())
//                    .createAt(System.currentTimeMillis())
//                    .cargoId(cargo.getCargoId())
//                    .username(username)
//                    .build();
//            orderInfoMapper.insert(orderInfo);
//            return orderInfo;
//        }
//    }

    /**
     * 事务性方法，用于支付订单
     *
     * @param username 用户名
     * @param orderId 订单ID
     * @return 订单信息
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OrderInfo payOrder(long username, long orderId) {
        // 根据订单ID查询订单信息
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);

        // 如果订单存在
        if (orderInfo != null) {
            // 获取订单余额
            BigDecimal balance = orderInfo.getBalance();

            // 根据用户ID支付余额
            Wallet wallet = balanceService.payByUser(username, balance);

            // 如果支付成功
            if (wallet != null) {
                // 更新订单状态为已支付
                orderInfo.setStatus(OrderStatus.PAID.getStatus());
                orderInfoMapper.updateById(orderInfo);

                // 发送已支付订单消息到RabbitMQ
                rabbitMQService.paidOrder(orderInfo.getOrderId());

                // 创建订单日志
                createOrderLog(username, orderId);

                return orderInfo;
            }
        }

        return null;
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

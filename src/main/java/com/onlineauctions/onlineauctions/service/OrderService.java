package com.onlineauctions.onlineauctions.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onlineauctions.onlineauctions.mapper.OrderInfoMapper;
import com.onlineauctions.onlineauctions.mapper.OrderMapper;
import com.onlineauctions.onlineauctions.pojo.PageList;
import com.onlineauctions.onlineauctions.pojo.type.OrderStatus;
import com.onlineauctions.onlineauctions.pojo.user.balance.Order;
import com.onlineauctions.onlineauctions.pojo.user.balance.OrderInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderInfoMapper orderInfoMapper;

    private final OrderMapper orderMapper;

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

    public void cancelOrder(String orderId) {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("orderId", orderId);
        orderInfoMapper.update(OrderInfo.builder().status(OrderStatus.PAID_TIMEOUT.getStatus()).build(), queryWrapper);
    }
}

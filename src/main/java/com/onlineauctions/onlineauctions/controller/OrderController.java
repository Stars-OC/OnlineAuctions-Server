package com.onlineauctions.onlineauctions.controller;

import com.onlineauctions.onlineauctions.annotation.Permission;
import com.onlineauctions.onlineauctions.annotation.RequestPage;
import com.onlineauctions.onlineauctions.annotation.RequestToken;
import com.onlineauctions.onlineauctions.pojo.PageInfo;
import com.onlineauctions.onlineauctions.pojo.PageList;
import com.onlineauctions.onlineauctions.pojo.Result;
import com.onlineauctions.onlineauctions.pojo.type.Role;
import com.onlineauctions.onlineauctions.pojo.user.balance.Order;
import com.onlineauctions.onlineauctions.pojo.user.balance.OrderInfo;
import com.onlineauctions.onlineauctions.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Validated
@Permission(Role.USER)
public class OrderController {

    private final OrderService orderService;

    /**
     * 查询订单信息
     *
     * @param username 用户名
     * @param orderId 订单ID
     * @return 订单信息Result对象
     */
    @GetMapping("/info/{orderId}")
    public Result<OrderInfo> orderInfo(@RequestToken long username, @PathVariable long orderId) {
        OrderInfo orderInfo = orderService.orderInfo(username, orderId);
        return orderInfo != null ? Result.success("查询成功", orderInfo): Result.failure("查询失败，订单不存在/不属于你");
    }

    /**
     * 查询订单列表
     *
     * @param username 用户名
     * @param pageInfo 分页信息对象
     * @return 结果对象，包含查询结果和分页列表
     */
    @GetMapping("/user/list")
    public Result<PageList<Order>> orderList(@RequestToken long username, @RequestPage PageInfo pageInfo) {
        PageList<Order> pageList = orderService.orderList(username,pageInfo.getPageNum(),pageInfo.getPageNum(),pageInfo.getFilter());
        return pageList != null ? Result.success("查询成功", pageList): Result.failure("查询失败");
    }
}

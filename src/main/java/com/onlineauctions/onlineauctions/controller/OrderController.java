package com.onlineauctions.onlineauctions.controller;

import com.onlineauctions.onlineauctions.annotation.Permission;
import com.onlineauctions.onlineauctions.annotation.RequestPage;
import com.onlineauctions.onlineauctions.annotation.RequestToken;
import com.onlineauctions.onlineauctions.pojo.PageList;
import com.onlineauctions.onlineauctions.pojo.PaidInfo;
import com.onlineauctions.onlineauctions.pojo.request.PayInfo;
import com.onlineauctions.onlineauctions.pojo.respond.PageInfo;
import com.onlineauctions.onlineauctions.pojo.respond.Result;
import com.onlineauctions.onlineauctions.pojo.type.Role;
import com.onlineauctions.onlineauctions.pojo.user.balance.OrderInfo;
import com.onlineauctions.onlineauctions.pojo.user.balance.Wallet;
import com.onlineauctions.onlineauctions.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Validated
@Permission(isAllowAll = true)
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
    public Result<OrderInfo> orderInfo(@RequestToken("username") long username, @PathVariable long orderId) {
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
    public Result<PageList<OrderInfo>> orderList(@RequestToken("username") long username, @RequestPage PageInfo pageInfo) {
        PageList<OrderInfo> pageList = orderService.orderList(username,pageInfo.getPageNum(),pageInfo.getPageSize());
        return pageList != null ? Result.success("查询成功", pageList): Result.failure("查询失败");
    }

    /**
     * 取消用户的订单
     *
     * @param username 用户名
     * @param orderId 订单ID
     * @return 取消结果
     */
    @GetMapping("/user/cancel/{orderId}")
    public Result<Wallet> cancelOrder(@RequestToken("username") long username, @PathVariable String orderId) {
        // 调用订单服务取消订单
        Wallet wallet = orderService.cancelOrderByUser(username, orderId);
        // 判断取消是否成功
        return wallet != null?Result.success("取消成功",wallet): Result.failure("取消失败，订单不存在/不属于你");
    }

    /**
     * 为用户支付订单
     *
     * @param username 用户名
     * @param payInfo 支付信息
     * @return 支付结果
     */
    @PostMapping ("/user/pay")
    public Result<OrderInfo> payOrder(@RequestToken("username") long username, @Validated @RequestBody PayInfo payInfo) {
        // 调用订单服务创建订单
        PaidInfo paidInfo = orderService.payOrder(username,payInfo.getOrderId(), payInfo.getPassword());
        // 判断创建是否成功
        return Result.decide(paidInfo.getSuccess(),paidInfo.getOrderInfo(), paidInfo.getMessage());
    }
}

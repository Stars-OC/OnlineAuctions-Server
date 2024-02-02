package com.onlineauctions.onlineauctions.receiver;

import com.onlineauctions.onlineauctions.config.RabbitMQConfig;
import com.onlineauctions.onlineauctions.pojo.type.OrderStatus;
import com.onlineauctions.onlineauctions.pojo.user.balance.OrderInfo;
import com.onlineauctions.onlineauctions.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderReceiver {

    private final OrderService orderService;

    @RabbitListener(queues = RabbitMQConfig.ORDER_DELAY_QUEUE, concurrency = "1")
    public void verifyOrderStatus(String orderId) {
        OrderInfo orderInfo = orderService.getOrderInfo(orderId);
        if (orderInfo == null) return;
        // 取消订单
        if (orderInfo.getStatus() == OrderStatus.PAYING.getStatus()){
            orderService.cancelOrder(orderId);
            log.info("订单号：{}，状态：{}，取消订单", orderInfo.getOrderId(), orderInfo.getStatus());
        }

    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE, concurrency = "1")
    public void updateOrderStatus(String orderId) {
        OrderInfo orderInfo = orderService.getOrderInfo(orderId);
        if (orderInfo == null) return;
        // 支付成功
        if (orderInfo.getStatus() == OrderStatus.PAYING.getStatus()){
//            orderService.updateOrderInfo(orderInfo);
            log.info("订单号：{}，状态：{}，支付成功", orderInfo.getOrderId(), orderInfo.getStatus());
        }
    }
}

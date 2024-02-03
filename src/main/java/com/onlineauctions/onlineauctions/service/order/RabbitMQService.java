package com.onlineauctions.onlineauctions.service.order;

import com.onlineauctions.onlineauctions.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitMQService {

    private final RabbitTemplate rabbitTemplate;

    /**
     * 发送已支付订单消息到RabbitMQ的ORDER_EXCHANGE交换器，使用ORDER_ROUTING_KEY路由键
     *
     * @param orderId 订单ID
     */
    public void paidOrder(Long orderId) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE, RabbitMQConfig.ORDER_ROUTING_KEY, orderId);
    }


    /**
     * 延迟发送订单消息到RabbitMQ的ORDER_DELAY_QUEUE队列
     *
     * @param orderId 订单ID
     */
    public void delayOrder(Long orderId) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_DELAY_QUEUE,orderId,message -> {
            message.getMessageProperties().setDelay(15 * 60 * 1000);
            return message;
        });
    }
}

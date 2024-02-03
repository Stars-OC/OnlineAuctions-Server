package com.onlineauctions.onlineauctions.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public final static String ORDER_QUEUE = "orderQueue";

    public final static String ORDER_DELAY_QUEUE = "orderDelayQueue";

    public final static String ORDER_EXCHANGE = "orderExchange";

    public final static String ORDER_ROUTING_KEY = "order";

    // 消息转换器 jwt-json带databind
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue orderQueue() {
        return new Queue("orderQueue", true);
    }

    @Bean
    public Queue orderDelayQueue() {
        return new Queue("orderDelayQueue", true);
    }

    @Bean
    DirectExchange orderDirectExchange(){
        // durable:是否持久化,默认是false,持久化交换机。
        // autoDelete:是否自动删除，交换机先有队列或者其他交换机绑定的时候，然后当该交换机没有队列或其他交换机绑定的时候，会自动删除。
        // arguments：交换机设置的参数，比如设置交换机的备用交换机（Alternate Exchange），当消息不能被路由到该交换机绑定的队列上时，会自动路由到备用交换机
        return new DirectExchange("orderDirectExchange",true,false);
    }

    @Bean
    Binding orderBinding(){
        return BindingBuilder.bind(orderQueue()).to(orderDirectExchange()).with(ORDER_ROUTING_KEY);
    }
}

package com.onlineauctions.onlineauctions;

import com.onlineauctions.onlineauctions.service.order.RabbitMQService;
import com.onlineauctions.onlineauctions.utils.AesUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OnlineAuctionsApplicationTests {

    @Autowired
    private RabbitMQService rabbitMQService;


    @Test
    void contextLoads() {
        rabbitMQService.delayOrder(1L);
    }

}

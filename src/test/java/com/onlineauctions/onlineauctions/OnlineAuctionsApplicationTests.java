package com.onlineauctions.onlineauctions;

import com.onlineauctions.onlineauctions.pojo.PaidInfo;
import com.onlineauctions.onlineauctions.pojo.user.balance.OrderInfo;
import com.onlineauctions.onlineauctions.service.auction.AuctionOperationService;
import com.onlineauctions.onlineauctions.service.order.OrderService;
import com.onlineauctions.onlineauctions.service.order.RabbitMQService;
import com.onlineauctions.onlineauctions.service.redis.AuctionRedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class OnlineAuctionsApplicationTests {

    @Autowired
    private AuctionOperationService auctionOperationService;

    @Autowired
    private AuctionRedisService auctionRedisService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Test
    void contextLoads() {
        System.out.println(redisTemplate.opsForValue().get("auction:3"));
        String auctionRedisServiceValue = auctionRedisService.getValue(String.valueOf(3));
        System.out.println(auctionRedisServiceValue);
//        Thread thread = new Thread(() -> {
//            for (int i = 0; i < 5; i++){
//                PaidInfo paidInfo = orderService.payOrder(1, 6, "123456");
//                System.out.println("1:" + paidInfo);
//            }
//        });
//        Thread thread2 = new Thread(() -> {
//            for (int i = 0; i < 5; i++){
//                PaidInfo paidInfo = orderService.payOrder(1, 6, "123456");
//                System.out.println("2:" + paidInfo);
//            }
//        });
//        thread.start();
//        thread2.start();
    }


}

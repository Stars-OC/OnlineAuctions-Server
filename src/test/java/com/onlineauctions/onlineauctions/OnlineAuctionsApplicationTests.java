package com.onlineauctions.onlineauctions;

import com.onlineauctions.onlineauctions.controller.AuctionOperationController;
import com.onlineauctions.onlineauctions.pojo.PaidInfo;
import com.onlineauctions.onlineauctions.pojo.request.AuctionOperation;
import com.onlineauctions.onlineauctions.pojo.respond.AuctionOperationResult;
import com.onlineauctions.onlineauctions.pojo.respond.Result;
import com.onlineauctions.onlineauctions.pojo.user.balance.OrderInfo;
import com.onlineauctions.onlineauctions.service.auction.AuctionOperationService;
import com.onlineauctions.onlineauctions.service.order.OrderService;
import com.onlineauctions.onlineauctions.service.order.RabbitMQService;
import com.onlineauctions.onlineauctions.service.redis.AuctionRedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.math.BigDecimal;

@SpringBootTest
class OnlineAuctionsApplicationTests {

    @Autowired
    private AuctionOperationService auctionOperationService;

    @Autowired
    private AuctionRedisService auctionRedisService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AuctionOperationController auctionOperationController;

    @Test
    void contextLoads() {
//        System.out.println(redisTemplate.opsForValue().get("auction:3"));
//        String auctionRedisServiceValue = auctionRedisService.getValue(String.valueOf(3));
//        System.out.println(auctionRedisServiceValue);
        Thread thread = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++){
                    AuctionOperation auctionOperation = new AuctionOperation();
                    auctionOperation.setAuctionId(5L);
                    int a = i*50;
                    int val = a + 200;
                    auctionOperation.setAdditionalPrice(new BigDecimal(val));
                    AuctionOperationResult<Object> objectAuctionOperationResult = auctionOperationService.auctionAdditionalPrice(1, auctionOperation);
                    System.out.println("1:" +  auctionOperation + ":" + objectAuctionOperationResult);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println("1: " + e.getMessage());
                    }
                }
            }catch (Exception e){
                System.out.println("1: " + e.getMessage());
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++){
                    AuctionOperation auctionOperation = new AuctionOperation();
                    auctionOperation.setAuctionId(5L);
                    int a = i*20;
                    int val = a + 200;
                    auctionOperation.setAdditionalPrice(new BigDecimal(val));
                    AuctionOperationResult<Object> objectAuctionOperationResult = auctionOperationService.auctionAdditionalPrice(123456, auctionOperation);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        System.out.println("2: " + e.getMessage());
                    }
                    System.out.println("2:" +  auctionOperation + ":" + objectAuctionOperationResult);

                }
            }catch (Exception e){
                System.out.println("2: " + e.getMessage());
            }
        });
        thread.start();
        thread2.start();
    }


}

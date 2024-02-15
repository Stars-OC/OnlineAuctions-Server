package com.onlineauctions.onlineauctions;

import com.onlineauctions.onlineauctions.config.LoginConfig;
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
import org.apache.catalina.core.ApplicationContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.math.BigDecimal;
import java.util.*;

@SpringBootTest
class OnlineAuctionsApplicationTests {

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private LoginConfig loginConfig;

    @Autowired
    private AuctionRedisService auctionRedisService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AuctionOperationController auctionOperationController;

    @Test
    void contextLoads() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();


//		List<String> urlList = new ArrayList<>();
//		for (RequestMappingInfo info : map.keySet()) {
//			// 获取url的Set集合，一个方法可能对应多个url
//			Set<String> patterns = info.getPatternsCondition().getPatterns();
//
//			for (String url : patterns) {
//				urlList.add(url);
//			}
//		}
//        List<String> urlList = new ArrayList<>();
//        for (RequestMappingInfo info : map.keySet()){
//            //获取url的Set集合，一个方法可能对应多个url
//            Set<String> patterns = info.getPatternsCondition().getPatterns();
//            for (String url : patterns){
//                urlList.add(url);
//            }
//        }
//        for (String s : urlList) {
//            System.out.println(s);
//        }

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            Map<String, String> map1 = new HashMap<String, String>();
            RequestMappingInfo info = m.getKey();
            HandlerMethod method = m.getValue();
            PatternsRequestCondition p = info.getPatternsCondition();
            for (String url : p.getPatterns()) {
                map1.put("url", url);
            }
            map1.put("className", method.getMethod().getDeclaringClass().getName()); // 类名
            map1.put("method", method.getMethod().getName()); // 方法名
            RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
            for (RequestMethod requestMethod : methodsCondition.getMethods()) {
                map1.put("type", requestMethod.toString());
            }

            list.add(map1);
        }

        for (Map<String, String> stringStringMap : list) {
            System.out.println(stringStringMap.toString());
        }
//
//        JSONArray jsonArray = JSONArray.fromObject(list);

//        System.out.println(redisTemplate.opsForValue().get("auction:3"));
//        String auctionRedisServiceValue = auctionRedisService.getValue(String.valueOf(3));
//        System.out.println(auctionRedisServiceValue);
//        Thread thread = new Thread(() -> {
//            try {
//                for (int i = 0; i < 10; i++){
//                    AuctionOperation auctionOperation = new AuctionOperation();
//                    auctionOperation.setAuctionId(5L);
//                    int a = i*50;
//                    int val = a + 200;
//                    auctionOperation.setAdditionalPrice(new BigDecimal(val));
//                    AuctionOperationResult<Object> objectAuctionOperationResult = auctionOperationService.auctionAdditionalPrice(1, auctionOperation);
//                    System.out.println("1:" +  auctionOperation + ":" + objectAuctionOperationResult);
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        System.out.println("1: " + e.getMessage());
//                    }
//                }
//            }catch (Exception e){
//                System.out.println("1: " + e.getMessage());
//            }
//        });
//        Thread thread2 = new Thread(() -> {
//            try {
//                for (int i = 0; i < 10; i++){
//                    AuctionOperation auctionOperation = new AuctionOperation();
//                    auctionOperation.setAuctionId(5L);
//                    int a = i*20;
//                    int val = a + 200;
//                    auctionOperation.setAdditionalPrice(new BigDecimal(val));
//                    AuctionOperationResult<Object> objectAuctionOperationResult = auctionOperationService.auctionAdditionalPrice(123456, auctionOperation);
//                    try {
//                        Thread.sleep(200);
//                    } catch (InterruptedException e) {
//                        System.out.println("2: " + e.getMessage());
//                    }
//                    System.out.println("2:" +  auctionOperation + ":" + objectAuctionOperationResult);
//
//                }
//            }catch (Exception e){
//                System.out.println("2: " + e.getMessage());
//            }
//        });
//        thread.start();
//        thread2.start();
    }


}

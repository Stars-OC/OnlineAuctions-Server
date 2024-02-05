package com.onlineauctions.onlineauctions.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class AuctionRedisService extends RedisService {


    public static final int TIME = 305;

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    public AuctionRedisService(StringRedisTemplate redisTemplate) {
        super(redisTemplate, "auction");
    }

    public void setAuctionValue( String key, String value) {
        setValue(key,value,TIME, TimeUnit.SECONDS);
    }
}

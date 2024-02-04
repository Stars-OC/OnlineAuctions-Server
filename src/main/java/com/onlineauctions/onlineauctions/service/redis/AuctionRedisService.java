package com.onlineauctions.onlineauctions.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuctionRedisService extends RedisService {


    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    public AuctionRedisService(StringRedisTemplate redisTemplate) {
        super(redisTemplate, "auction");
    }
}

package com.onlineauctions.onlineauctions.service.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuctionRedisService extends RedisHashService{

    @Autowired
    public AuctionRedisService(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate, "auction");
    }
}

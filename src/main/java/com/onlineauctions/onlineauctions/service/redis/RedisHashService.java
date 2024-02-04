package com.onlineauctions.onlineauctions.service.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class RedisHashService {

    private final RedisTemplate<String, String> redisTemplate;

    private final String HASH_KEY;

    public void setHashValue( String field, String value) {
        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
        hashOps.put(HASH_KEY, field, value);

    }

    public void setHashValue( String field, String value, long time, TimeUnit timeUnit) {
        setHashValue(field, value);
        redisTemplate.expire(HASH_KEY, time, timeUnit);
    }

    public String getHashValue(String field) {
        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
        return (String) hashOps.get(HASH_KEY, field);
    }

    public void deleteHashField( String field) {
        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
        hashOps.delete(HASH_KEY, field);
    }
}

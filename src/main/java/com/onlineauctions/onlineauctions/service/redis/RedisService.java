package com.onlineauctions.onlineauctions.service.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redis;

    private final String PREFIX;

    public String getRealKey(String key){
        StringBuffer buffer = new StringBuffer();
        return  buffer.append(PREFIX).append(":").append(key).toString();
    }

    public void setValue( String key, String value) {
        redis.opsForValue().set(getRealKey(key), value);
    }

    public void setValue( String key, String value, long time, TimeUnit timeUnit) {
        redis.opsForValue().set(getRealKey(key), value,time, timeUnit);
    }

    public String getValue(String key) {
        return redis.opsForValue().get(getRealKey(key));
    }

    public void deleteKey(String key) {
        redis.opsForValue().getOperations().delete(getRealKey(key));
    }

    public Long getKeyTime(String key) {
        return redis.getExpire(getRealKey(key));
    }
}

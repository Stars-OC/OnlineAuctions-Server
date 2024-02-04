package com.onlineauctions.onlineauctions.config;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 配置Redis模板以支持事务。
     */
    @Bean
    public void getRedisTemplate(){
        stringRedisTemplate.setEnableTransactionSupport(true);
    }


}

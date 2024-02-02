package com.onlineauctions.onlineauctions.service;

import com.onlineauctions.onlineauctions.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceService {

    private final UserMapper userMapper;

    private final StringRedisTemplate redisTemplate;



}

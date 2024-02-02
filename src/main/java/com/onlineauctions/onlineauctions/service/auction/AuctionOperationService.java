package com.onlineauctions.onlineauctions.service.auction;

import com.onlineauctions.onlineauctions.mapper.AuctionLogMapper;
import com.onlineauctions.onlineauctions.mapper.AuctionMapper;
import com.onlineauctions.onlineauctions.mapper.CargoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuctionOperationService {

    private final AuctionLogMapper auctionLogMapper;

    private final AuctionMapper auctionMapper;

    private final CargoMapper cargoMapper;

    private final StringRedisTemplate redisTemplate;
}

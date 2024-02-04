package com.onlineauctions.onlineauctions.service.auction;

import com.onlineauctions.onlineauctions.mapper.AuctionLogMapper;
import com.onlineauctions.onlineauctions.mapper.AuctionMapper;
import com.onlineauctions.onlineauctions.mapper.CargoMapper;
import com.onlineauctions.onlineauctions.pojo.respond.AuctionStateInfo;
import com.onlineauctions.onlineauctions.service.redis.AuctionRedisService;
import com.onlineauctions.onlineauctions.service.redis.RedisHashService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuctionOperationService {

    private final AuctionLogMapper auctionLogMapper;

    private final AuctionMapper auctionMapper;

    private final CargoMapper cargoMapper;

    private final AuctionRedisService auctionRedisService;

    public AuctionStateInfo getNowAuctionInfo(long auctionId) {
        auctionRedisService.setHashValue(String.valueOf(auctionId), "1");
        return null;

    }
}

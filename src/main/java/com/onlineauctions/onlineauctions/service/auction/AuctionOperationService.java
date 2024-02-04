package com.onlineauctions.onlineauctions.service.auction;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.onlineauctions.onlineauctions.annotation.Permission;
import com.onlineauctions.onlineauctions.mapper.AuctionLogMapper;
import com.onlineauctions.onlineauctions.mapper.AuctionMapper;
import com.onlineauctions.onlineauctions.mapper.CargoMapper;
import com.onlineauctions.onlineauctions.pojo.auction.Auction;
import com.onlineauctions.onlineauctions.pojo.respond.AuctionStateInfo;
import com.onlineauctions.onlineauctions.pojo.type.Role;
import com.onlineauctions.onlineauctions.service.redis.AuctionRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Permission(Role.USER)
public class AuctionOperationService {

    private final AuctionLogMapper auctionLogMapper;

    private final AuctionMapper auctionMapper;

    private final CargoMapper cargoMapper;

    private final AuctionRedisService auctionRedisService;

    @Transactional
    public AuctionStateInfo getNowAuctionInfo(long auctionId) {
        String value = auctionRedisService.getValue(String.valueOf(auctionId));
        if (StringUtils.isEmpty(value))  return null;

        Auction auction = auctionMapper.selectById(auctionId);

        auctionRedisService.setValue(String.valueOf(auctionId), "1");
        return null;

    }
}

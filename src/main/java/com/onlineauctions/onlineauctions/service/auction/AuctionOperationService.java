package com.onlineauctions.onlineauctions.service.auction;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.onlineauctions.onlineauctions.annotation.Permission;
import com.onlineauctions.onlineauctions.mapper.AuctionLogMapper;
import com.onlineauctions.onlineauctions.mapper.AuctionMapper;
import com.onlineauctions.onlineauctions.mapper.CargoMapper;
import com.onlineauctions.onlineauctions.pojo.auction.Auction;
import com.onlineauctions.onlineauctions.pojo.respond.AuctionStateInfo;
import com.onlineauctions.onlineauctions.pojo.type.AuctionStatus;
import com.onlineauctions.onlineauctions.pojo.type.Role;
import com.onlineauctions.onlineauctions.service.redis.AuctionRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
@Permission(Role.USER)
public class AuctionOperationService {

    private final AuctionLogMapper auctionLogMapper;

    private final AuctionMapper auctionMapper;

    private final CargoMapper cargoMapper;

    private final AuctionRedisService auctionRedisService;

    /**
     * 获取当前拍卖信息
     *
     * @param auctionId 拍卖ID
     * @return AuctionStateInfo 当前拍卖状态信息，如果不存在则返回null
     */
    @Transactional
    public AuctionStateInfo getNowAuctionInfo(long auctionId) {
        String key = String.valueOf(auctionId);
        String value = auctionRedisService.getValue(key);
        if (StringUtils.isEmpty(value))  return null;

        Auction auction = auctionMapper.selectById(auctionId);

        if (auction.getStatus() == AuctionStatus.SELLING.getStatus()) {
            long keyTime = auctionRedisService.getKeyTime(key);
            long nowTime = System.currentTimeMillis() / 1000;
            long endTime = nowTime + keyTime - 5;
            return new AuctionStateInfo(new BigDecimal(value), keyTime, endTime);
        }

        return null;
    }
}

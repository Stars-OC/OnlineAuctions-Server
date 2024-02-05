package com.onlineauctions.onlineauctions.service.auction;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.onlineauctions.onlineauctions.mapper.AuctionLogMapper;
import com.onlineauctions.onlineauctions.mapper.AuctionMapper;
import com.onlineauctions.onlineauctions.mapper.CargoMapper;
import com.onlineauctions.onlineauctions.mapper.WalletMapper;
import com.onlineauctions.onlineauctions.pojo.auction.Auction;
import com.onlineauctions.onlineauctions.pojo.auction.AuctionLog;
import com.onlineauctions.onlineauctions.pojo.request.AuctionOperation;
import com.onlineauctions.onlineauctions.pojo.respond.AuctionOperationResult;
import com.onlineauctions.onlineauctions.pojo.respond.AuctionStateInfo;
import com.onlineauctions.onlineauctions.pojo.type.AuctionStatus;
import com.onlineauctions.onlineauctions.pojo.type.CargoStatus;
import com.onlineauctions.onlineauctions.pojo.user.balance.Wallet;
import com.onlineauctions.onlineauctions.service.order.OrderService;
import com.onlineauctions.onlineauctions.service.order.RabbitMQService;
import com.onlineauctions.onlineauctions.service.redis.AuctionRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuctionOperationService {

    private final AuctionLogMapper auctionLogMapper;

    private final AuctionMapper auctionMapper;

    private final CargoMapper cargoMapper;

    private final WalletMapper walletMapper;

    private final AuctionRedisService auctionRedisService;

    private final RabbitMQService rabbitMQService;
    private final OrderService orderService;

    /**
     * 获取当前拍卖信息
     *
     * @param auctionId 拍卖ID
     * @return AuctionStateInfo 当前拍卖状态信息，如果不存在则返回null
     */
    @Transactional
    public AuctionStateInfo getNowAuctionInfo(long auctionId) {
        String key = String.valueOf(auctionId);
        System.out.println(key);
        String value = auctionRedisService.getValue(key);
        System.out.println(value);
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

    /**
     * 对拍卖进行额外加价操作
     *
     * @param username 用户名
     * @param auctionOperation 拍卖操作对象
     * @return 拍卖操作结果对象
     */
    @Transactional
    public AuctionOperationResult<Object> auctionAdditionalPrice(long username, AuctionOperation auctionOperation) {

        BigDecimal auctionOperationAdditionalPrice = auctionOperation.getAdditionalPrice();
        // 获取拍卖信息
        Long auctionId = auctionOperation.getAuctionId();
        Auction auction = auctionMapper.selectById(auctionId);

        // 获取用户钱包信息
        Wallet wallet = walletMapper.selectById(username);
        // 获取用户筹码
        BigDecimal fundPrice = wallet.getFund().add(wallet.getMoney());
        // 判断筹码是否充足
        if (fundPrice.compareTo(auctionOperationAdditionalPrice) < 0) {
            return AuctionOperationResult.builder().message("加价失败，你所拥有的筹码不足").info(wallet).build();
        }

        // 加锁
        // 获取拍卖的实时价格
        String value = auctionRedisService.getValue(auctionId.toString());
        if (StringUtils.isEmpty(value))  return AuctionOperationResult.builder().message("当前拍卖已结束").info(auction).build();

        // 获取一些关于价格的信息
        BigDecimal nowPrice = new BigDecimal(value);
        BigDecimal additionalPrice = auctionOperationAdditionalPrice.subtract(nowPrice);
        BigDecimal shouldAdditionalPrice = auction.getAdditionalPrice();
        // 判断加价幅度是否大于加价幅度
        if (shouldAdditionalPrice.compareTo(additionalPrice) > 0) {
            return AuctionOperationResult.builder().message("加价失败，当前加价金额小于加价幅度").info(auction).build();
        }

        // 加价 放入redis中
        auctionRedisService.setAuctionValue(auctionId.toString(), auctionOperationAdditionalPrice.toString());
        // 放锁
        // 记录加价记录
        AuctionLog auctionLog = AuctionLog.builder()
                .auctionId(auctionId)
                .bidder(username)
                .price(auctionOperationAdditionalPrice)
                .build();
        auctionLogMapper.insert(auctionLog);
        return AuctionOperationResult.builder().success(true).message("加价成功").info(auctionLog).build();

    }

    /**
     * 拍卖会结束
     *
     * @param auctionId 拍卖会ID
     */
    @Transactional
    public void cancelAuction(String auctionId) {

        // 查找拍卖会信息
        Auction auction = auctionMapper.selectById(auctionId);
        // 查找最后的加价记录
        QueryWrapper<AuctionLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("auction_id", auctionId).orderByDesc("create_at");
        AuctionLog auctionLog = auctionLogMapper.selectOne(queryWrapper);

        // 更新拍卖会结束时间为当前时间戳
        auction.setEndTime(System.currentTimeMillis() / 1000);

        Long cargoId = auction.getCargoId();
        // 如果没有交易信息，那说明没人买
        if (auctionLog == null) {
            auction.setStatus(AuctionStatus.UNSOLD.getStatus());
            auctionMapper.updateById(auction);
            cargoMapper.updateCargoStatus(cargoId, CargoStatus.UNSOLD.getStatus());
            rabbitMQService.paidOrder(auction.getAuctionId());
            return;
        }
        // 更新拍卖会状态为已售出
        auction.setStatus(AuctionStatus.SOLD.getStatus());
        // 更新拍卖会落槌价为最后的加价金额
        auction.setHammerPrice(auctionLog.getPrice());
        // 更新拍卖会信息
        auctionMapper.updateById(auction);

        // 更新货物状态为已售出
        cargoMapper.updateCargoStatus(cargoId, CargoStatus.SOLD.getStatus());
        // 创建订单信息
        orderService.createOrderInfoByAuction(auctionLog.getBidder(), Long.parseLong(auctionId));
    }
}

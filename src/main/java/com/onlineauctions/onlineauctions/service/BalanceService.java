package com.onlineauctions.onlineauctions.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onlineauctions.onlineauctions.mapper.OrderInfoMapper;
import com.onlineauctions.onlineauctions.mapper.OrderMapper;
import com.onlineauctions.onlineauctions.mapper.UserMapper;
import com.onlineauctions.onlineauctions.mapper.WalletMapper;
import com.onlineauctions.onlineauctions.pojo.PageList;
import com.onlineauctions.onlineauctions.pojo.auction.Cargo;
import com.onlineauctions.onlineauctions.pojo.user.balance.Order;
import com.onlineauctions.onlineauctions.pojo.user.balance.OrderInfo;
import com.onlineauctions.onlineauctions.pojo.user.balance.Wallet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceService {

    private final UserMapper userMapper;

    private final StringRedisTemplate redisTemplate;

    private final WalletMapper walletMapper;




    /**
     * 查询钱包信息
     *
     * @param username 用户名
     * @return 钱包信息对象
     */
    public Wallet walletInfo(long username) {
        return walletMapper.selectById(username);
    }

    /**
     * 行政充值
     *
     * @param username 用户名
     * @param money 要充值的金额
     * @return 充值后的钱包信息对象
     */
    public Wallet walletRechargeByAdmin(long username, BigDecimal money) {
        Wallet wallet = walletMapper.selectById(username);
        if (wallet == null) return null;
        wallet.setMoney(wallet.getMoney().add(money));
        walletMapper.updateById(wallet);
        return wallet;
    }

}

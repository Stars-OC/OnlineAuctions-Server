package com.onlineauctions.onlineauctions.service.order;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.onlineauctions.onlineauctions.mapper.UserMapper;
import com.onlineauctions.onlineauctions.mapper.WalletMapper;
import com.onlineauctions.onlineauctions.pojo.user.balance.Wallet;
import com.onlineauctions.onlineauctions.utils.AesUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    /**
     * 用户支付
     *
     * @param username 用户名
     * @param balance 需要支付的金额
     * @param password 密码
     * @return 支付成功返回钱包对象，否则返回null
     */
    @Transactional
    public Wallet payByUser(long username, BigDecimal balance, String password) {
        // 构建查询条件
        QueryWrapper<Wallet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username).eq("password", AesUtil.encrypt(password));
        // 查询钱包信息
        Wallet wallet = walletMapper.selectOne(queryWrapper);
        BigDecimal money = wallet.getMoney();
        // 判断余额是否足够支付
        if (money.compareTo(balance) >= 0) {
            // 更新钱包余额
            wallet.setMoney(money.subtract(balance));
            // 更新钱包信息
            walletMapper.updateById(wallet);
            return wallet;
        } else {
            // 余额不足
            return null;
        }
    }


    /**
     * 取消已支付订单并更新钱包余额和信誉值
     *
     * @param username 用户名
     * @param balance 订单价值
     * @return 更新后的钱包对象
     */
    @Transactional
    public Wallet cancelOrderPaid(long username, BigDecimal balance) {
        // 根据用户名查询钱包
        Wallet wallet = walletMapper.selectById(username);

        // 更新钱包余额 - 15%的货物价值
        wallet.setMoney(wallet.getMoney().subtract(balance.multiply(new BigDecimal("0.15"))));
        // 更新信誉值 - 40%的货物价值
        wallet.setFund(wallet.getFund().subtract(balance.multiply(new BigDecimal("0.4"))));

        // 更新钱包信息
        walletMapper.updateById(wallet);
        return wallet;
    }

}

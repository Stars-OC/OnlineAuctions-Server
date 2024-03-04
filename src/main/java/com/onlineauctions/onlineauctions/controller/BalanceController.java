package com.onlineauctions.onlineauctions.controller;

import com.onlineauctions.onlineauctions.annotation.Permission;
import com.onlineauctions.onlineauctions.annotation.RequestToken;
import com.onlineauctions.onlineauctions.pojo.respond.Result;
import com.onlineauctions.onlineauctions.pojo.type.Role;
import com.onlineauctions.onlineauctions.pojo.user.balance.Wallet;
import com.onlineauctions.onlineauctions.service.order.BalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@Slf4j
@RequestMapping("/api/user/balance")
@RequiredArgsConstructor
@Validated
public class BalanceController {

    private final BalanceService balanceService;

    @PostMapping("/wallet/create")
    @Permission(isAllowAll = true)
    public Result<Wallet> createWallet(@RequestToken("username") long username,String password) {
        return balanceService.createWallet(username,password)?Result.success("创建钱包成功"):Result.failure("创建钱包失败");
    }

    /**
     * 获取钱包信息
     *
     * @param username 用户名
     * @return 钱包信息Result对象
     */
    @GetMapping("/wallet/info")
    @Permission(isAllowAll = true)
    public Result<Wallet> walletInfo(@RequestToken("username") long username) {
        Wallet wallet = balanceService.walletInfo(username);
        return wallet!=null ? Result.success("查询成功",wallet):Result.failure("查询失败");
    }

    /**
     * 行政充值
     *
     * @param username 用户名
     * @param money 要充值的金额
     * @return 钱包信息Result对象
     */
    @PostMapping("/wallet/recharge")
    @Permission(value = Role.ADMIN, isIndividual = true)
    public Result<Wallet> walletRecharge(long username, String money) {
        Wallet wallet = balanceService.walletRechargeByAdmin(username, new BigDecimal(money));
        return wallet != null ? Result.success("充值成功", wallet): Result.failure("充值失败，账户不存在");
    }




}

package com.onlineauctions.onlineauctions.controller;

import com.onlineauctions.onlineauctions.annotation.Permission;
import com.onlineauctions.onlineauctions.pojo.respond.AuctionStateInfo;
import com.onlineauctions.onlineauctions.pojo.respond.Result;
import com.onlineauctions.onlineauctions.pojo.type.Role;
import com.onlineauctions.onlineauctions.service.auction.AuctionOperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/auction/operation")
@RequiredArgsConstructor
@Validated
@Permission(Role.USER)
public class AuctionOperationController {

    private final AuctionOperationService auctionOperationService;

    @GetMapping("/info/{auctionId}")
    public Result<AuctionStateInfo> getNowAuctionInfo(@PathVariable long auctionId){
        AuctionStateInfo nowAuctionInfo = auctionOperationService.getNowAuctionInfo(auctionId);
        return nowAuctionInfo != null? Result.success("获取成功",nowAuctionInfo):Result.failure("获取失败，拍卖未开始/已结束");
    }
}

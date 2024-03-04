package com.onlineauctions.onlineauctions.controller;

import com.onlineauctions.onlineauctions.annotation.Permission;
import com.onlineauctions.onlineauctions.annotation.RequestToken;
import com.onlineauctions.onlineauctions.pojo.request.AuctionOperation;
import com.onlineauctions.onlineauctions.pojo.respond.AuctionOperationResult;
import com.onlineauctions.onlineauctions.pojo.respond.AuctionStateInfo;
import com.onlineauctions.onlineauctions.pojo.respond.Result;
import com.onlineauctions.onlineauctions.pojo.type.Role;
import com.onlineauctions.onlineauctions.service.auction.AuctionOperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/auction/operation")
@RequiredArgsConstructor
@Validated
@Permission(isAllowAll = true)
public class AuctionOperationController {

    private final AuctionOperationService auctionOperationService;

    @GetMapping("/info/{auctionId}")
    public Result<AuctionStateInfo> getNowAuctionInfo(@PathVariable long auctionId) throws InterruptedException {
        AuctionStateInfo nowAuctionInfo = auctionOperationService.getNowAuctionInfo(auctionId);
        return nowAuctionInfo != null? Result.success("获取成功",nowAuctionInfo):Result.failure("获取失败，拍卖未开始/已结束");
    }

    @PostMapping("/add")
    public Result<Object> auctionAdditionalPrice(@RequestToken("username")long username, @Validated @RequestBody  AuctionOperation auctionOperation) throws InterruptedException {
        AuctionOperationResult<Object> result = auctionOperationService.auctionAdditionalPrice(username, auctionOperation);
        log.info("username:{} , post:{} : result{}",username,auctionOperation,result);
        return Result.decide(result.isSuccess(), result.getInfo(), result.getMessage());
    }
}

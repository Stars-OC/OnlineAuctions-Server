package com.onlineauctions.onlineauctions.controller;

import com.onlineauctions.onlineauctions.annotation.Permission;
import com.onlineauctions.onlineauctions.annotation.RequestPage;
import com.onlineauctions.onlineauctions.pojo.request.AuctionAndCargo;
import com.onlineauctions.onlineauctions.pojo.respond.PageInfo;
import com.onlineauctions.onlineauctions.pojo.PageList;
import com.onlineauctions.onlineauctions.pojo.respond.Result;
import com.onlineauctions.onlineauctions.pojo.auction.Auction;
import com.onlineauctions.onlineauctions.pojo.auction.AuctionLog;
import com.onlineauctions.onlineauctions.pojo.type.Role;
import com.onlineauctions.onlineauctions.service.auction.AuctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/auction")
@RequiredArgsConstructor
@Validated
public class AuctionController {

    private final AuctionService auctionService;

    /**
     * 获取拍卖信息
     *
     * @param auctionId 拍卖ID
     * @return 返回拍卖信息Result对象
     */
    @GetMapping("/info/{auctionId}")
    public Result<Auction> getAuctionInfo(@PathVariable("auctionId") Long auctionId) {
        // 调用auctionService的getAuctionInfo方法获取拍卖信息
        Auction auction = auctionService.getAuctionInfo(auctionId);
        // 如果auction不为空，返回一个成功的Result对象，包含auction；否则返回一个失败的Result对象
        return auction !=null? Result.success(auction) : Result.failure();
    }

    /**
     * 根据cargoId获取拍卖信息
     *
     * @param cargoId 货物ID
     * @return 拍卖对象
     */
    @GetMapping("/info/auction/{cargoId}")
    public Result<Auction> getAuctionInfoByCargoId(@PathVariable("cargoId") Long cargoId) {
        // 调用auctionService的getAuctionInfo方法获取拍卖信息
        Auction auction = auctionService.getAuctionInfoByCargoId(cargoId);
        // 如果auction不为空，返回一个成功的Result对象，包含auction；否则返回一个失败的Result对象
        return auction !=null? Result.success(auction) : Result.failure();
    }

    /**
     * 获取 已发布的 拍卖列表
     *
     * @return 返回拍卖列表Result对象
     */
    @GetMapping("/published/list")
    public Result<PageList<Auction>> publishedAuctionList(@RequestPage PageInfo pageInfo) {
        // 调用auctionService的getAuctionList方法获取拍卖列表
        PageList<Auction> auctionList = auctionService.auctionList(pageInfo.getPageNum(), pageInfo.getPageSize(),pageInfo.getFilter(),true);
        // 如果auctionList不为空，返回一个成功的Result对象，包含auctionList；否则返回一个失败的Result对象
        return auctionList !=null? Result.success(auctionList) : Result.failure();
    }

//    /**
//     * 获取 待审核的 拍卖列表
//     *
//     * @return 返回拍卖列表Result对象
//     */
//    @Permission(Role.AUDIT_ADMIN)
//    @GetMapping("/audit/list")
//    public Result<PageList<Auction>> auditAuctionList(@RequestPage PageInfo pageInfo) {
//        // 调用auctionService的getAuctionList方法获取拍卖列表
//        PageList<Auction> auctionList = auctionService.auctionList(pageInfo.getPageNum(), pageInfo.getPageSize(),pageInfo.getFilter(),false);
//        // 如果auctionList不为空，返回一个成功的Result对象，包含auctionList；否则返回一个失败的Result对象
//        return auctionList !=null? Result.success(auctionList) : Result.failure();
//    }

    /**
     * 审核 添加 拍卖场次
     *
     * @param auctionAndCargo 拍卖对象
     * @return 返回一个Result对象，包含审核结果
     */
    @Permission(Role.AUDIT_ADMIN)
    @PostMapping("/audit/add")
    public Result<Auction> auditAuction(@Validated @RequestBody AuctionAndCargo auctionAndCargo) {
        // 调用auctionService的auditCargo方法审核拍卖
        boolean result = auctionService.auditCargo(auctionAndCargo);
        // 如果result不为空，返回一个成功的Result对象，包含result；否则返回一个失败的Result对象
        return result ? Result.success("添加成功",auctionAndCargo.getAuction()) : Result.failure();
    }

    /**
     * 获取拍卖日志列表
     *
     * @param auctionId 拍卖ID
     * @param pageInfo Page相关的封装
     * @return 返回拍卖日志列表PageList对象
     */
    @Permission(Role.USER)
    @PostMapping("/info/log/{auctionId}")
    public Result<PageList<AuctionLog>> auctionLog(@PathVariable("auctionId") Long auctionId, @RequestPage PageInfo pageInfo) {
        // 调用auctionService的getAuctionLog方法获取拍卖日志
        PageList<AuctionLog> pageList = auctionService.auctionLogList(auctionId, pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getFilter());
        // 如果auction不为空，返回一个成功的Result对象，包含auction；否则返回一个失败的Result对象
        return pageList != null ? Result.success(pageList) : Result.failure();
    }



}

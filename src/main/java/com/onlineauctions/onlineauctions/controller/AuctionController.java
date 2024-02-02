package com.onlineauctions.onlineauctions.controller;

import com.onlineauctions.onlineauctions.annotation.RequestPage;
import com.onlineauctions.onlineauctions.pojo.PageInfo;
import com.onlineauctions.onlineauctions.pojo.PageList;
import com.onlineauctions.onlineauctions.pojo.Result;
import com.onlineauctions.onlineauctions.pojo.auction.Auction;
import com.onlineauctions.onlineauctions.service.AuctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @param auctionID 拍卖ID
     * @return 返回拍卖信息Result对象
     */
    @GetMapping("/info/{auctionID}")
    public Result<Auction> getAuctionInfo(@PathVariable("auctionID") Long auctionID) {
        // 调用auctionService的getAuctionInfo方法获取拍卖信息
        Auction auction = auctionService.getAuctionInfo(auctionID);
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

    /**
     * 获取 待审核的 拍卖列表
     *
     * @return 返回拍卖列表Result对象
     */
    @GetMapping("/audit/list")
    public Result<PageList<Auction>> auditAuctionList(@RequestPage PageInfo pageInfo) {
        // 调用auctionService的getAuctionList方法获取拍卖列表
        PageList<Auction> auctionList = auctionService.auctionList(pageInfo.getPageNum(), pageInfo.getPageSize(),pageInfo.getFilter(),false);
        // 如果auctionList不为空，返回一个成功的Result对象，包含auctionList；否则返回一个失败的Result对象
        return auctionList !=null? Result.success(auctionList) : Result.failure();
    }
}

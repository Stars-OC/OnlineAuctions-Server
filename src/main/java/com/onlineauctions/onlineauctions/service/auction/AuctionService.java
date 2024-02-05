package com.onlineauctions.onlineauctions.service.auction;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onlineauctions.onlineauctions.mapper.AuctionLogMapper;
import com.onlineauctions.onlineauctions.mapper.AuctionMapper;
import com.onlineauctions.onlineauctions.mapper.CargoMapper;
import com.onlineauctions.onlineauctions.pojo.PageList;
import com.onlineauctions.onlineauctions.pojo.auction.Auction;
import com.onlineauctions.onlineauctions.pojo.auction.AuctionLog;
import com.onlineauctions.onlineauctions.pojo.auction.Cargo;
import com.onlineauctions.onlineauctions.pojo.request.AuctionAndCargo;
import com.onlineauctions.onlineauctions.pojo.type.AuctionStatus;
import com.onlineauctions.onlineauctions.pojo.type.CargoStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionLogMapper auctionLogMapper;

    private final AuctionMapper auctionMapper;

    private final CargoMapper cargoMapper;


    /**
     * 获取拍卖列表
     *
     * @param pageNum 当前页码
     * @param pageSize 每页数量
     * @param filter 过滤条件
     * @param published 是否只查询已发布的拍卖
     * @return 拍卖列表
     */
    public PageList<Auction> auctionList(int pageNum, int pageSize, String filter, boolean published) {
        // 创建查询条件
        QueryWrapper<Auction> queryWrapper = new QueryWrapper<>();
        // 过滤条件
        if (!StringUtils.isEmpty(filter)) queryWrapper.like("name", filter);
        // 查询已发布 和 拍卖的
        if (published) {
            queryWrapper.between("status", AuctionStatus.PUBLISHED.getStatus(), AuctionStatus.SELLING.getStatus());
            queryWrapper.orderByAsc("start_time");
        } else {
            queryWrapper.orderByAsc("create_time");
        }

        // 创建分页对象
        Page<Auction> userPage = new Page<>(pageNum, pageSize);

        // 执行查询并获取分页结果
        Page<Auction> selectPage = auctionMapper.selectPage(userPage, queryWrapper);
        // 返回分页列表
        return new PageList<>(selectPage);
    }

    /**
     * 获取拍卖信息 <开启拍卖>
     *
     * @param auctionID 拍卖ID
     * @return 拍卖信息
     */
    @Transactional
    public Auction getAuctionInfo(Long auctionID) {
        // 根据拍卖ID查询拍卖信息
        Auction auction = auctionMapper.selectById(auctionID);

        if (auction == null) return null;
        // 用来解锁拍卖信息
        long nowTime = System.currentTimeMillis()/1000;
        if (auction.getStatus() == AuctionStatus.PUBLISHED.getStatus() && auction.getEndTime() > nowTime && auction.getStartTime() <= nowTime){
            // 查询对应的货物信息
            Cargo cargo = cargoMapper.selectById(auction.getCargoId());
            // 设置货物状态为正在销售
            cargo.setStatus(CargoStatus.SELLING.getStatus());
            // 更新货物信息
            cargoMapper.updateById(cargo);
            // 设置拍卖已开始
            auction.setStatus(AuctionStatus.SELLING.getStatus());
            auctionMapper.updateById(auction);
        }

        return auction;
    }

    /**
     * 审核货物
     *
     * @param auctionAndCargo 装载有拍卖和货物的对象
     * @return 审核是否成功
     */
    @Transactional
    public boolean auditCargo(AuctionAndCargo auctionAndCargo) {
        Auction auction = auctionAndCargo.getAuction();
        Cargo cargo = auctionAndCargo.getCargo();
        boolean a = auctionMapper.insert(auction) > 0;
        boolean b = cargoMapper.updateById(cargo) > 0;
        return a && b;
    }

    /**
     * 获取拍卖日志列表
     *
     * @param auctionId 拍卖ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param filter 过滤条件
     * @return 返回拍卖日志列表PageList对象
     */
    public PageList<AuctionLog> auctionLogList(Long auctionId, int pageNum, int pageSize, String filter) {
        // 创建查询条件
        QueryWrapper<AuctionLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("auction_id", auctionId);
        // 过滤条件
        if (!StringUtils.isEmpty(filter)) queryWrapper.like("bidder", filter);
        // 创建分页对象
        Page<AuctionLog> page = new Page<>(pageNum, pageSize);
        // 执行查询并获取分页结果
        Page<AuctionLog> selectPage = auctionLogMapper.selectPage(page, queryWrapper);
        // 返回分页结果的包装对象
        return new PageList<>(selectPage);
    }

    /**
     * 根据cargoId获取拍卖信息
     *
     * @param cargoId 货物ID
     * @return 拍卖对象
     */
    public Auction getAuctionInfoByCargoId(Long cargoId) {
        QueryWrapper<Auction> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cargo_id", cargoId);
        return auctionMapper.selectOne(queryWrapper);
    }

    public Auction getAuctionInfoByAuctionId(Long auctionId) {
        return auctionMapper.selectById(auctionId);
    }

    public Auction getAuctionInfoByAuctionIdWithLock(long auctionId) {
        return getAuctionInfoByAuctionId(auctionId);
    }
}

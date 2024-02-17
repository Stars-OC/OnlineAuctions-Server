package com.onlineauctions.onlineauctions.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.onlineauctions.onlineauctions.pojo.auction.Auction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuctionMapper extends BaseMapper<Auction> {
    List<Auction> auctionListByUserLog(long username, int pageNum, int pageSize);

}

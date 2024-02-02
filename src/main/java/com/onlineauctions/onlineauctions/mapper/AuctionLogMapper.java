package com.onlineauctions.onlineauctions.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.onlineauctions.onlineauctions.pojo.auction.AuctionLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuctionLogMapper extends BaseMapper<AuctionLog> {
}

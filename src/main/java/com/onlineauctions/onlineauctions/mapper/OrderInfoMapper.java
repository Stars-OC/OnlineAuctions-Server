package com.onlineauctions.onlineauctions.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.onlineauctions.onlineauctions.pojo.user.balance.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
}

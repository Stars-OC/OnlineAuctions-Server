package com.onlineauctions.onlineauctions.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.onlineauctions.onlineauctions.pojo.user.balance.Order;
import com.onlineauctions.onlineauctions.pojo.user.balance.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    List<OrderInfo> getOrderInfoList(long username, int pageNum, int pageSize);

}

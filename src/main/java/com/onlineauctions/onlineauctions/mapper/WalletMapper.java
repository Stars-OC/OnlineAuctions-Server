package com.onlineauctions.onlineauctions.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.onlineauctions.onlineauctions.pojo.user.balance.Wallet;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WalletMapper extends BaseMapper<Wallet> {
}

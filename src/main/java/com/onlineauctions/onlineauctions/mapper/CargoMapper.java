package com.onlineauctions.onlineauctions.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.onlineauctions.onlineauctions.pojo.auction.Cargo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CargoMapper extends BaseMapper<Cargo> {

    @Update("update cargo set status = #{status} where cargo_id = #{cargoId}")
    int updateCargoStatus(Long cargoId, Integer status);
}

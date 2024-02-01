package com.onlineauctions.onlineauctions.pojo.auction;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("cargo")
public class Cargo {

    private Long cargoId;
    private String name;
    private String description;
    private String resource;
    private String type;
    private Long sellerId;
    private BigDecimal startingPrice;
    private BigDecimal additionalPrice;
    private BigDecimal hammerPrice;
    private Long startTime;
    private Long endTime;
    private Long createAt;
    private Long updateAt;
    private Long status;
    private Integer deleted;

}

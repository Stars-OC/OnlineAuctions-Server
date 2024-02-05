package com.onlineauctions.onlineauctions.pojo.auction;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@TableName("auction")
public class Auction {

    @TableId(type = IdType.AUTO)
    private Long auctionId;

    @NotNull(message = "拍卖物品id不能为空")
    private Long cargoId;

    @NotNull(message = "起拍价不能为空")
    private BigDecimal startingPrice;

    @NotNull(message = "加价幅度不能为空")
    private BigDecimal additionalPrice;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal hammerPrice;

    private Long startTime;

    private Long endTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer status;
}

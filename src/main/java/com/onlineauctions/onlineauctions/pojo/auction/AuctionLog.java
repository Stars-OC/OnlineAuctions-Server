package com.onlineauctions.onlineauctions.pojo.auction;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("auction_log")
public class AuctionLog {

    private Long auctionId;

    @NotNull(message = "买家id不能为空")
    private Long bidder;

    @NotNull(message = "加价不能为空")
    private BigDecimal price;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT,updateStrategy = FieldStrategy.NEVER)
    private Long createAt;
}

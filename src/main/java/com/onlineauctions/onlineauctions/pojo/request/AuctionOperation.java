package com.onlineauctions.onlineauctions.pojo.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AuctionOperation {

    @NotNull(message = "auctionId不能为空")
    private Long auctionId;

    @NotNull(message = "加价幅度不能为空")
    private BigDecimal additionalPrice;

}

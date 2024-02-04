package com.onlineauctions.onlineauctions.pojo.respond;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AuctionStateInfo {

    private BigDecimal balance;
    private Long updateAt;
    private Long endTime;

}

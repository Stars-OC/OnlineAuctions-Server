package com.onlineauctions.onlineauctions.pojo.respond;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionStateInfo {

    private BigDecimal balance;
    private Long updateAt;
    private Long endTime;

}

package com.onlineauctions.onlineauctions.pojo.auction;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("auction")
public class Auction {

    private Long auctionId;
    private Long cargoId;
    private Long bidder;
    private BigDecimal price;
    private Long createAt;
}

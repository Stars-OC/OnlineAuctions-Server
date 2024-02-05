package com.onlineauctions.onlineauctions.pojo.request;

import com.onlineauctions.onlineauctions.pojo.auction.Auction;
import com.onlineauctions.onlineauctions.pojo.auction.Cargo;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuctionAndCargo {
    @NotNull(message = "拍卖场不能为空")
    private Auction auction;
    @NotNull(message = "货物不能为空")
    private Cargo cargo;
}

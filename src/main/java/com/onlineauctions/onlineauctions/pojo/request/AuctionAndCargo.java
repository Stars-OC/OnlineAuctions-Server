package com.onlineauctions.onlineauctions.pojo.request;

import com.onlineauctions.onlineauctions.pojo.auction.Auction;
import com.onlineauctions.onlineauctions.pojo.auction.Cargo;
import lombok.Data;

@Data
public class AuctionAndCargo {

    private Auction auction;
    private Cargo cargo;
}

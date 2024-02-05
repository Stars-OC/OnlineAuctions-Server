package com.onlineauctions.onlineauctions.pojo.respond;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuctionOperationResult<T> {
    private boolean success;
    private String message;
    private T info;
}

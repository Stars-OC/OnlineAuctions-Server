package com.onlineauctions.onlineauctions.pojo.request;

import com.onlineauctions.onlineauctions.pojo.user.balance.OrderInfo;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PaidInfo {
    private String payType;
    private Boolean success;
    private String message;
    private OrderInfo orderInfo;
}

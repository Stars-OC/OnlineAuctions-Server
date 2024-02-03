package com.onlineauctions.onlineauctions.pojo.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PayInfo {
    @NotNull(message = "商品Id不能为空")
    private Long orderId;
    @NotEmpty(message = "支付密码不能为空")
    private String password;
}

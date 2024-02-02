package com.onlineauctions.onlineauctions.pojo.type;

import lombok.Getter;

public enum OrderStatus {

    ERROR(-1,"未知错误"),

    PAYING(0,"等待支付"),

    PAID(1,"支付完成"),

    PAY_FAILED(2,"支付失败"),

    PAID_TIMEOUT(3,"支付超时"),

    CANCELED(4,"已取消"),

    CANCELED_ERROR(5,"被不可知原因中止");


    @Getter
    private final int status;

    @Getter
    private final String message;

    OrderStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String toString() {
        return String.valueOf(status);
    }

}

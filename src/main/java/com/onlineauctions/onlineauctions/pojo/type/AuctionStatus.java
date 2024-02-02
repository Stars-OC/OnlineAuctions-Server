package com.onlineauctions.onlineauctions.pojo.type;

import lombok.Getter;

public enum AuctionStatus {

    ERROR(-1,"未知错误"),

    AUDIT(0,"正在审核中"),

    PUBLISHED(1,"等待拍卖中"),

    SELLING(2,"正在拍卖中"),

    SOLD(3,"已卖出"),

    UNSOLD(4,"流拍"),

    CANCELLED(5,"被不可知原因中止");


    @Getter
    private final int status;

    @Getter
    private final String message;

    AuctionStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String toString() {
        return String.valueOf(status);
    }

}

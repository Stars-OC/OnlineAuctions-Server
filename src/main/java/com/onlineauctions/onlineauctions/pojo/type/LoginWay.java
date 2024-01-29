package com.onlineauctions.onlineauctions.pojo.type;

import lombok.Getter;

/**
 * 登录类型枚举类
 */
public enum LoginWay {
    //TODO 后面将其与mp进行映射
    /**
     * 普通登录
     */
    NORMAL_LOGIN(1, "普通登录"),
    /**
     * 微信登录
     */
    WECHAT_LOGIN(2, "微信登录"),
    /**
     * 手机号登录
     */
    PHONE_LOGIN(3, "手机号登录"),
    /**
     * 微信扫码登录
     */
    WECHAT_SCAN_LOGIN(4, "微信扫码登录"),
    /**
     * 微信扫码登录
     */
    WECHAT_QRCODE_LOGIN(5, "微信二维码登录"),
    /**
     * 微信扫码登录
     */
    WECHAT_QRCODE_LOGIN_BY_CODE(6, "微信二维码登录(通过code)")
    ;
    /**自定义状态码**/
    @Getter
    private final int code;

    /**
     * 携 带 消 息
     */
    @Getter
    private final String loginWay;

    /**
     * 构造方法
     * @param code 状态码
     * @param loginWay 登录方式
     */
    LoginWay(int code, String loginWay) {

        this.code = code;

        this.loginWay = loginWay;
    }

}

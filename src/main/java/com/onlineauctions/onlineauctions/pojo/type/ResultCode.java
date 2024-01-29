package com.onlineauctions.onlineauctions.pojo.type;

import lombok.Getter;

/**
 * Desc: 统 一 返 回 状 态 码
 */
public enum ResultCode {
    /**操作成功**/
    RC200(200,"操作成功"),
    /**操作失败**/
    RC999(999,"操作失败"),
    /**授权规则不通过**/
    RC204(204,"授权规则不通过,请稍后再试!"),
    /**access_denied**/
    RC403(403,"无访问权限,请联系管理员授予权限"),
    /**错误代码**/
    RC404(404,"错误代码"),
    /**access_denied**/
    RC401(401,"匿名用户访问无权限资源时的异常"),

    /**请求参数不正确**/
    RC406(406,"请求参数不正确"),
    /**服务异常**/
    RC500(500,"系统异常，请稍后重试"),

    /**
     * 访问token无效
     */
    TOKEN_INVALID(4001,"访问token无效"),

    /**
     * 访问token不合法
     */
    INVALID_TOKEN(4002,"访问token不合法"),
    /**
     * 访问token过期
     */
    TOKEN_EXPIRED(4003,"访问token过期"),
    TOKEN_SIGN_ERROR(4004,"签名不匹配"),
    /**
     * 没有权限访问该资源
     */
    ACCESS_DENIED(2004,"没有权限访问该资源"),
    /**
     * 客户端认证失败
     */
    CLIENT_AUTHENTICATION_FAILED(1001,"客户端认证失败"),
    /**
     * 用户名或密码错误
     */
    USERNAME_OR_PASSWORD_ERROR(1002,"用户名或密码错误"),

    UNSUPPORTED_GRANT_TYPE(1003, "不支持的认证模式");

    /**自定义状态码**/
    @Getter
    private final int code;

    /**
     * 携 带 消 息
     */
    @Getter
    private final String message;
    /**
     * 构 造 方 法
     */
    ResultCode(int code, String message) {

        this.code = code;

        this.message = message;
    }
}

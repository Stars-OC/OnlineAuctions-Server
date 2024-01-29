package com.onlineauctions.onlineauctions.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlineauctions.onlineauctions.pojo.type.ResultCode;
import lombok.Data;

import java.io.Serializable;

/**
 * Desc: Ajax 返 回 JSON 结 果 封 装 数 据
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {

    /**
     * 是否返回成功
     */
    private boolean success;

    /**
     * 错误状态
     */
    private int code;

    /***
     * 错误信息
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 成功的操作
     */
    public static <T> Result<T> success() {
        return  success(null);
    }

    /**
     * 成 功 操 作 , 携 带 数 据
     */
    public static <T> Result<T> success(T data){
        return success(ResultCode.RC200.getMessage(),data);
    }

    /**
     * 成 功 操 作, 携 带 消 息
     */
    public static <T> Result<T> success(String message) {
        return success(message, null);
    }

    /**
     * 成 功 操 作, 携 带 消 息 和 携 带 数 据
     */
    public static <T> Result<T> success(String message, T data) {
        return success(ResultCode.RC200.getCode(), message, data);
    }

    /**
     * 成 功 操 作, 携 带 自 定 义 状 态 码 和 消 息
     */
    public static <T> Result<T> success(int code, String message) {
        return success(code, message, null);
    }

    public static <T> Result<T> success(int code,String message,T data) {
        Result<T> result = new Result<T>();
        result.setCode(code);
        result.setMsg(message);
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

    /**
     * 失 败 操 作, 默 认 数 据
     */
    public static <T> Result<T> failure() {
        return failure(ResultCode.RC200.getMessage());
    }

    /**
     * 失 败 操 作, 携 带 自 定 义 消 息
     */
    public static <T> Result<T> failure(String message) {
        return failure(message, null);
    }

    /**
     * 失 败 操 作, 携 带 自 定 义 消 息 和 数 据
     */
    public static <T> Result<T> failure(String message, T data) {
        return failure(ResultCode.RC999.getCode(), message, data);
    }

    /**
     * 失 败 操 作, 携 带 自 定 义 状 态 码 和 自 定 义 消 息
     */
    public static <T> Result<T> failure(int code, String message) {
        return failure(code, message, null);
    }

    /**
     * 失 败 操 作, 携 带 自 定 义 状 态 码 , 消 息 和 数 据
     */
    public static <T> Result<T> failure(int code, String message, T data) {
        Result<T> result = new Result<T>();
        result.setCode(code);
        result.setMsg(message);
        result.setSuccess(false);
        result.setData(data);
        return result;
    }

    /**
     * Boolean 返 回 操 作, 携 带 默 认 返 回 值
     */
    public static <T> Result<T> decide(boolean b) {
        return decide(b, ResultCode.RC200.getMessage(), ResultCode.RC999.getMessage());
    }

    /**
     * 根据给定的条件决定返回成功结果还是失败结果
     *
     * @param b           条件
     * @param success     成功结果的字符串
     * @param failure     失败结果的字符串
     * @param <T>         结果类型
     * @return            如果条件为真，返回一个成功结果，否则返回一个失败结果
     */
    public static <T> Result<T> decide(boolean b, String success, String failure) {
        if (b) {
            return success(success);
        } else {
            return failure(failure);
        }
    }


    /**
     * 生成一个带有结果码的Result对象
     *
     * @param b     是否成功
     * @param resultCode  结果码
     * @param <T>   结果对象类型
     * @return 生成的Result对象
     */
    public static <T> Result<T> resultCode(boolean b, ResultCode resultCode) {
        Result<T> result = new Result<T>();
        result.setCode(resultCode.getCode());
        result.setMsg(resultCode.getMessage());
        result.setSuccess(b);
        return result;
    }

    /**
     * 将给定的ResultCode封装为一个成功的Result对象。
     *
     * @param resultCode 传入的ResultCode对象
     * @param <T>       泛型类型
     * @return 包含给定ResultCode对象的Result对象
     */
    public static <T> Result<T> codeSuccess(ResultCode resultCode) {
        return resultCode(true, resultCode);
    }


        /**
     * 根据给定的错误码返回一个失败的结果对象。
     *
     * @param resultCode 错误码
     * @param <T> 结果对象的数据类型
     * @return 失败的结果对象
     */
    public static <T> Result<T> codeFailure(ResultCode resultCode) {
        return resultCode(false, resultCode);
    }

}

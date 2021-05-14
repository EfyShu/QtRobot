package com.efy.function.dto;

import lombok.Data;

import java.util.List;

/**
 * Author : Efy Shu
 * Date : 2021/5/10 22:32
 * Description :
 **/
@Data
public class Result<T> {
    private String status;
    private String code;
    private String message;
    private T data;

    public static <T> Result<T> ok(){
        return ok(null);
    }

    public static <T> Result<T> ok(String message){
        return ok(message,null);
    }

    public static <T> Result<T> ok(T data){
        return ok(null,data);
    }

    public static <T> Result<T> ok(String message,T data){
        return build("ok",null,message,data);
    }

    public static <T> Result<T> fail(){
        return fail(null);
    }

    public static <T> Result<T> fail(String message){
        return build("fail",null,message,null);
    }

    public static <T> Result<T> fail(String code,String message,T data){
        return build("fail",code,message,data);
    }

    public static <T> Result<T> build(String status,String code,String message,T Data){
        Result<T> result = new Result();
        result.setStatus(status);
        result.setCode(code);
        result.setMessage(message);
        result.setData(Data);
        return result;
    }

    @Override
    public String toString() {
        String listLenth = null;
        if(data instanceof List && ((List)data).size() > 5){
            listLenth = ((List)data).size()+"";
        }
        return "Result(" +
                "status=" + status +
                ", code=" + code  +
                ", message=" + message +
                ", data=" + ((listLenth != null) ? "size:["+listLenth+"]" : data) +
                ')';
    }
}

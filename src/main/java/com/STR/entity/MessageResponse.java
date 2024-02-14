package com.STR.entity;

import lombok.Data;

@Data
public class MessageResponse {
    //状态码
    private int code;
    //返回消息体
    private String message;
    public MessageResponse(int code ,String message) {
        this.code = code;
        this.message = message;
    }
    //设置状态码
    public MessageResponse Code(int code){
        this.code = code;
        return this;
    }
}

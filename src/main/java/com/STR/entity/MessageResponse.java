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
}

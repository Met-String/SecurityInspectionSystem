package com.STR.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseBody {
    // 状态码
    private int code;
    // 返回消息
    private String message;
    // 返回数据
    private Object data;
}

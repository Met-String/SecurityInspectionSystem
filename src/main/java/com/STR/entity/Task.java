package com.STR.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    int task_id;
    String name;
    int user_id;
    String description;
    // 任务是否为激活态 0未激活 1激活
    int state;

    // 数据库中无 便于前端展示
    String user_name;
    // 数据库中无 便于前端展示 创建任务
    Set<Integer> sites;
}

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
    // 创建任务必须
    String name;
    // 创建任务必须
    int user_id;
    String description;
    LocalDateTime create_time;
    int organization_id;
    int creator_id;
    // 数据库中无，创建任务必须
    Set<Integer> sites;
    // 数据库中无，创建任务必须
    Set<LocalDateTime> dateTimeSet;
}

package com.STR.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskSiteInstance {
    int tasksiteinstance_id;
    int taskinstance_id;
    int site_id;
    /*
        0未开始 1完成无异常 2发生工单（发起一个新的工单）3更新工单（跟踪、更新一个工单的信息）
        4跳检（由于不可抗力、无法到达等） 5漏检（当前时间超出规定巡检日期）
    */
    int state;
    LocalDateTime check_time;
    int user_id;

    // 数据库中不存在这一项
    double longitude;
    // 数据库中不存在这一项
    double latitude;
    // 数据库中不存在这一项
    String user_name;
    // 数据库中不存在这一项
    String site_name;
    // 数据库中不存在这一项 便于数据处理
    NormalInspection normalInspection;
}

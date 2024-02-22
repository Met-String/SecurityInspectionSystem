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
    int status;
    LocalDateTime check_time;
    int user_id;
    // 数据库中不存在这一项，但是是每次点位巡查时必须的常规检查
    NormalInspection normalInspection;
}

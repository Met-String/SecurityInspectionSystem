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
    // 数据库中不存在这一项，需要通过taskinstance_id到taskinstance表中进行查询。
    int user_id;
}

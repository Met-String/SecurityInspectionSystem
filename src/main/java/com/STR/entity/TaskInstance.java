package com.STR.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskInstance {
    int taskinstance_id;
    int task_id;
    LocalDateTime start_time;
    LocalDateTime end_time;
    int user_id;
    // 任务完成情况 0 为待完成 1 为完成 2 为已超时
    int state;

    // 下面这一项在数据库中不存在 为了便于任务查看、发放为必须
    String task_name;
    // 下面这一项在数据库中不存在 为了便于任务查看、发放为必须
    String task_description;
    // 下面这一项在数据库中不存在 为了便于任务查看、发放为必须 TaskInstance和其下属TaskSiteInstance一般绑定出现
    List<TaskSiteInstance> taskSiteInstances;
}

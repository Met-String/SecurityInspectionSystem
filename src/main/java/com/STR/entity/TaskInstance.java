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
public class TaskInstance {
    int taskinstance_id;
    int task_id;
    LocalDateTime start_time;
    LocalDateTime end_time;
    int user_id;
    // 0为未完成 1为完成
    int status;
    /* 下面这一项在数据库中不存在，由于根据设想的逻辑，每一个Task会生成一系列不一的TaskInstance，而不是一系列完全相同的TaskInstance
     * 因此每一个TaskInstance都会有自己独特的TaskSiteInstance，也就是独特的点位列表
     * 因此这里这一项属性即为点位列表。而具体如何生成每个TaskInstance独特的点位列表，还需要后续算法完成。
     */
    Set<Integer> sites;
}

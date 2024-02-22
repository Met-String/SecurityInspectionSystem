package com.STR.service;

import com.STR.entity.NormalInspection;
import com.STR.entity.Task;
import com.STR.entity.TaskInstance;
import com.STR.entity.TaskSiteInstance;

import java.util.List;
import java.util.Map;

public interface TaskService {
    // 添加新任务
    void addNewTask(Task task);

    // 根据用户名、时间戳条件查找指定日期、用户的任务实例
    List<TaskInstance> findTaskInstance(Map<String,Object> map);

    // 查找一个点位的巡检历史
    List<TaskSiteInstance> findHistoryOfSite(int site_id);

    // 完成一个点位实例的巡检任务
    int finishTaskSiteInstance(TaskSiteInstance taskSiteInstance);
}

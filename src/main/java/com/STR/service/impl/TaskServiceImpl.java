package com.STR.service.impl;

import com.STR.entity.Task;
import com.STR.entity.TaskInstance;
import com.STR.entity.TaskSite;
import com.STR.entity.TaskSiteInstance;
import com.STR.mapper.TaskInstanceMapper;
import com.STR.mapper.TaskMapper;
import com.STR.mapper.TaskSiteInstanceMapper;
import com.STR.mapper.TaskSiteMapper;
import com.STR.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

// 任务相关的服务类
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskMapper taskMapper;
    private final TaskSiteMapper taskSiteMapper;
    private final TaskInstanceMapper taskInstanceMapper;
    private final TaskSiteInstanceMapper taskSiteInstanceMapper;
    public TaskServiceImpl(TaskMapper taskMapper, TaskSiteMapper taskSiteMapper, TaskInstanceMapper taskInstanceMapper, TaskSiteInstanceMapper taskSiteInstanceMapper) {
        this.taskMapper = taskMapper;
        this.taskSiteMapper = taskSiteMapper;
        this.taskInstanceMapper = taskInstanceMapper;
        this.taskSiteInstanceMapper = taskSiteInstanceMapper;
    }
    // 添加新任务，以及一系列的任务点位，以及一系列单一的TaskInstance，以及一系列TaskSiteInstance
    @Override
    public void addNewTask(Task task) {
        taskMapper.insertTask(task);
        int taskID = task.getTask_id();
        int userID = task.getUser_id();
        // 插入一系列TaskSite
        for (Integer siteID : task.getSites()){
            taskSiteMapper.insertTaskSite(TaskSite.builder()
                    .task_id(taskID)
                    .site_id(siteID)
                    .build());
        }
        // 根据日期插入一系列TaskInstance
        for(LocalDateTime localDateTime : task.getDateTimeSet()){
            TaskInstance taskInstance = TaskInstance.builder()
                    .start_time(localDateTime)
                    .task_id(taskID)
                    .user_id(userID)
                    .build();
            taskInstanceMapper.insertTaskInstance(taskInstance);
            int taskInstanceID = taskInstance.getTaskinstance_id();

            // 同时，每个TaskInstance又要对应创建一系列TaskSiteInstance
            for (int site_id : task.getSites()){
                TaskSiteInstance taskSiteInstance = TaskSiteInstance.builder()
                        .taskinstance_id(taskInstanceID)
                        .site_id(site_id)
                        .build();
                taskSiteInstanceMapper.insertTaskSiteInstance(taskSiteInstance);
            }
        }
    }
}

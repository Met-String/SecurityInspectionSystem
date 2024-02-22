package com.STR.service.impl;

import com.STR.entity.*;
import com.STR.mapper.*;
import com.STR.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

// 任务相关的服务类
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskMapper taskMapper;
    private final TaskSiteMapper taskSiteMapper;
    private final TaskInstanceMapper taskInstanceMapper;
    private final TaskSiteInstanceMapper taskSiteInstanceMapper;
    private final NormalInspectionMapper normalInspectionMapper;
    public TaskServiceImpl(TaskMapper taskMapper, TaskSiteMapper taskSiteMapper, TaskInstanceMapper taskInstanceMapper, TaskSiteInstanceMapper taskSiteInstanceMapper, NormalInspectionMapper normalInspectionMapper, NormalInspectionMapper normalInspectionMapper1) {
        this.taskMapper = taskMapper;
        this.taskSiteMapper = taskSiteMapper;
        this.taskInstanceMapper = taskInstanceMapper;
        this.taskSiteInstanceMapper = taskSiteInstanceMapper;
        this.normalInspectionMapper = normalInspectionMapper;
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
                        .user_id(userID)
                        .build();
                taskSiteInstanceMapper.insertTaskSiteInstance(taskSiteInstance);
            }
        }
    }

    // 根据用户、时间戳找到巡检任务实例，可用于一个用户查看自己今天有什么任务
    @Override
    public List<TaskInstance> findTaskInstance(Map<String, Object> map) {
        List<TaskInstance> taskInstances = taskInstanceMapper.findTaskInstance(map);
        for (TaskInstance taskInstance : taskInstances){
            List<TaskSiteInstance> taskSiteInstances = taskSiteInstanceMapper.findTaskSiteInstanceByTaskInstanceID(taskInstance.getTaskinstance_id());
            taskInstance.setTaskSiteInstances(taskSiteInstances);
        }
        return taskInstances;
    }

    // 查找一个点位的巡检历史
    @Override
    public List<TaskSiteInstance> findHistoryOfSite(int site_id) {
        List<TaskSiteInstance> taskSiteInstances = taskSiteInstanceMapper.findTaskSiteInstanceBySiteID(site_id);
        // 分别补全每个巡检点位实例的NormalInspection记录
        for (TaskSiteInstance taskSiteInstance : taskSiteInstances){
            //补全NormalInspection属性
            taskSiteInstance.setNormalInspection(normalInspectionMapper.findNormalInspectionByTaskSiteInstanceID(taskSiteInstance.getTasksiteinstance_id())) ;
        }
        return taskSiteInstances;
    }

    // 完成一个点位实例的巡检任务，其中点位实例必需要有TaskSiteInstanceID、CheckTime属性
    @Override
    public int finishTaskSiteInstance(TaskSiteInstance taskSiteInstance) {
        TaskSiteInstance MySiteInstance = taskSiteInstanceMapper.findByID(taskSiteInstance.getTasksiteinstance_id());
        if(MySiteInstance.getStatus() == 1){
            return 1;
        }
        NormalInspection normalInspection = taskSiteInstance.getNormalInspection();
        normalInspection.setTasksiteinstance_id(taskSiteInstance.getTasksiteinstance_id());
        taskSiteInstanceMapper.updateTaskSiteInstance(taskSiteInstance);
        normalInspectionMapper.insertNormalInspection(normalInspection);
        return 0;
    }
}

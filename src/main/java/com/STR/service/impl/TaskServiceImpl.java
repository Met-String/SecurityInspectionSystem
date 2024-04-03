package com.STR.service.impl;

import com.STR.entity.*;
import com.STR.mapper.*;
import com.STR.service.TaskService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
    private final StringRedisTemplate redisTemplate;
    public TaskServiceImpl(TaskMapper taskMapper, TaskSiteMapper taskSiteMapper, TaskInstanceMapper taskInstanceMapper, TaskSiteInstanceMapper taskSiteInstanceMapper, NormalInspectionMapper normalInspectionMapper, NormalInspectionMapper normalInspectionMapper1, StringRedisTemplate redisTemplate) {
        this.taskMapper = taskMapper;
        this.taskSiteMapper = taskSiteMapper;
        this.taskInstanceMapper = taskInstanceMapper;
        this.taskSiteInstanceMapper = taskSiteInstanceMapper;
        this.normalInspectionMapper = normalInspectionMapper;
        this.redisTemplate = redisTemplate;
    }

    // 这里只添加新任务+点位池 后续的TaskInstance生成交给DailyTaskManager模块完成
    @Override
    public void addNewTask(Task task) {
        taskMapper.insertTask(task);
        int taskID = task.getTask_id();
        // 插入一系列TaskSite
        for (int siteID : task.getSites()){
            taskSiteMapper.insertTaskSite(TaskSite.builder()
                    .task_id(taskID)
                    .site_id(siteID)
                    .build());
        }
    }

    @Override
    public List<Task> findByCondition(Map map) {
        List<Task> taskList = taskMapper.selectByCondition(map);
        for (Task task : taskList){
            task.setSites(taskSiteMapper.findSiteIDListByTaskID(task.getTask_id()));
        }
        return taskList;
    }
    // 根据条件找到任务Task


    // 根据条件找到巡检任务实例TaskInstance 用于:用户查看自己今天有什么任务
    @Override
    public List<TaskInstance> findInstanceByCondition(Map<String, Object> map) {
        List<TaskInstance> taskInstances = taskInstanceMapper.findByCondition(map);
        // 为每个Task实例插入巡检点位实例
        for (TaskInstance taskInstance : taskInstances){
            Map<String,Object> map1 = new HashMap<>();
            map1.put("taskinstance_id",taskInstance.getTaskinstance_id());
            List<TaskSiteInstance> taskSiteInstances = taskSiteInstanceMapper.findByCondition(map1);
            // 补全每个巡检点位实例的NormalInspection记录(可能没有)
            for (TaskSiteInstance taskSiteInstance : taskSiteInstances){
                taskSiteInstance.setNormalInspection(normalInspectionMapper.findByTaskSiteInstanceID(taskSiteInstance.getTasksiteinstance_id())) ;
            }
            taskInstance.setTaskSiteInstances(taskSiteInstances);
        }
        return taskInstances;
    }

    // 完成一个点位实例的巡检任务，其中点位实例必需要有TaskSiteInstanceID、CheckTime属性
    // 【缺失工单相关逻辑】
    @Override
    public int finishTaskSiteInstance(TaskSiteInstance taskSiteInstance) {
        Map<String,Object> map = new HashMap<>();
        map.put("tasksiteinstance_id",taskSiteInstance.getTasksiteinstance_id());
        TaskSiteInstance MySiteInstance = taskSiteInstanceMapper.findByCondition(map).get(0);
        // 不能重复完成
        if(MySiteInstance.getState() != 0){
            return 1;
        }
        // 插入NormalInspection 有可能为空(跳检 工单等)
        if(taskSiteInstance.getNormalInspection() != null){
            normalInspectionMapper.insertNormalInspection(taskSiteInstance.getNormalInspection());
        }
        taskSiteInstanceMapper.update(taskSiteInstance);
        // 除去Redis中对应Taskinstance的相关缓存
        redisTemplate.opsForSet().remove(String.valueOf(taskSiteInstance.getTaskinstance_id()),String.valueOf(taskSiteInstance.getTasksiteinstance_id()));
        return 0;
    }
}

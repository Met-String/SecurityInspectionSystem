package com.STR.service.impl;

import com.STR.Module.DailyTaskManager;
import com.STR.entity.*;
import com.STR.mapper.*;
import com.STR.service.TaskService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// 任务相关的服务类
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskMapper taskMapper;
    private final TaskSiteMapper taskSiteMapper;
    private final TaskInstanceMapper taskInstanceMapper;
    private final TaskSiteInstanceMapper taskSiteInstanceMapper;
    private final NormalInspectionMapper normalInspectionMapper;
    private final StringRedisTemplate redisTemplate;
    private final SiteMapper siteMapper;
    private final DailyTaskManager dailyTaskManager;
    public TaskServiceImpl(TaskMapper taskMapper, TaskSiteMapper taskSiteMapper, TaskInstanceMapper taskInstanceMapper, TaskSiteInstanceMapper taskSiteInstanceMapper, NormalInspectionMapper normalInspectionMapper, NormalInspectionMapper normalInspectionMapper1, StringRedisTemplate redisTemplate, SiteMapper siteMapper, DailyTaskManager dailyTaskManager) {
        this.taskMapper = taskMapper;
        this.taskSiteMapper = taskSiteMapper;
        this.taskInstanceMapper = taskInstanceMapper;
        this.taskSiteInstanceMapper = taskSiteInstanceMapper;
        this.normalInspectionMapper = normalInspectionMapper;
        this.redisTemplate = redisTemplate;
        this.siteMapper = siteMapper;
        this.dailyTaskManager = dailyTaskManager;
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
    public int deleteTask(Task task) {
        taskSiteMapper.deleteByTaskID(task.getTask_id());
        return taskMapper.deleteByTaskID(task.getTask_id());
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
            //为每个TaskInstance

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
        // 不能重复完成的验证逻辑
        Map<String,Object> map = new HashMap<>();
        map.put("tasksiteinstance_id",taskSiteInstance.getTasksiteinstance_id());
        TaskSiteInstance MySiteInstance = taskSiteInstanceMapper.findByCondition(map).get(0);
        if(MySiteInstance.getState() != 0){
            return 1;
        }
        // 插入NormalInspection 有可能为空(跳检 工单等)
        if(taskSiteInstance.getNormalInspection() != null){
            normalInspectionMapper.insertNormalInspection(taskSiteInstance.getNormalInspection());
        }

        // 更新taskSiteInstance 和Site的CheckTime相关信息
        taskSiteInstanceMapper.update(taskSiteInstance);
        Map<String,Object> map1 = new HashMap<>();
        map1.put("site_id",taskSiteInstance.getSite_id());
        Site site = siteMapper.selectByCondition(map1).get(0);
        site.setLast_check_time(taskSiteInstance.getCheck_time());
        // 计算并更新NextCheckDate
        dailyTaskManager.Next_Check_Date(site);

        // 除去Redis中对应Taskinstance的相关缓存
        redisTemplate.opsForSet().remove(String.valueOf(taskSiteInstance.getTaskinstance_id()),String.valueOf(taskSiteInstance.getTasksiteinstance_id()));
        // 如果对应Taskinstance已经为空 那么标记任务完成
        Set<String> taskSiteInstanceIDSet = redisTemplate.opsForSet().members(String.valueOf(taskSiteInstance.getTaskinstance_id()));
        if (taskSiteInstanceIDSet.isEmpty()){
            TaskInstance taskInstance = TaskInstance.builder()
                    .taskinstance_id(taskSiteInstance.getTaskinstance_id())
                    .end_time(LocalDateTime.now())
                    .state(1)
                    .build();
            taskInstanceMapper.update(taskInstance);
        }
        return 0;
    }

    // 激活任务 并不涉及复杂的信息素矩阵操作
    @Override
    public int activateTask(Task task) {
        return taskMapper.update(task);
    }
}

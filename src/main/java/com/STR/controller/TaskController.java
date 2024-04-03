package com.STR.controller;

import com.STR.entity.*;
import com.STR.service.SiteService;
import com.STR.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("task")
public class TaskController {
    private final TaskService taskService;
    private final SiteService siteService;

    public TaskController(TaskService taskService, SiteService siteService) {
        this.taskService = taskService;
        this.siteService = siteService;
    }

    // 创建新任务，包含一系列的TaskSite任务点
    @PostMapping("/add")
    public ResponseEntity<?> createTask(@RequestBody Task task){
        System.out.println(task.toString());
        taskService.addNewTask(task);
        return ResponseEntity.ok().body(new  MessageResponse(0,"成功创建任务！"));
    }

    // 获取此Task对应的SitesPool
    @GetMapping("/sitespool/{task_id}")
    public ResponseEntity<?> getSitesPoolByTask(@PathVariable("task_id") int task_id){
        List<Site> siteList = siteService.selectByTaskID(task_id);
        return ResponseEntity.ok().body(new MessageResponseBody(0,"成功获取点位池！", siteList));
    }

    // 获取所有Task
    @GetMapping("/")
    public ResponseEntity<?> getTask(){
        Map<String, Object> map = new HashMap<>();
        List<Task> taskList = taskService.findByCondition(map);
        return ResponseEntity.ok().body(new MessageResponseBody(0,"成功获取所有任务！", taskList));
    }


    // 按照日期、用户名获取巡检任务实例
    @GetMapping("/taskinstance")
    public ResponseEntity<?> getTodayTaskInstanceOfSpecUser(
            @RequestParam(required = false)LocalDateTime timestamp,
            @RequestParam(required = false)Integer user_id){
        Map<String, Object> map = new HashMap<>();
        if (timestamp != null) map.put("timestamp", timestamp);
        if (user_id != null) map.put("user_id", user_id);
        List<TaskInstance> taskInstance = taskService.findInstanceByCondition(map);
        return ResponseEntity.ok().body(new MessageResponseBody(0, "获取任务实例成功", taskInstance));
    }
}

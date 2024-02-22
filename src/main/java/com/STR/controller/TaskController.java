package com.STR.controller;

import com.STR.entity.MessageResponse;
import com.STR.entity.MessageResponseBody;
import com.STR.entity.Task;
import com.STR.entity.TaskInstance;
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

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // 创建新任务，包含一系列的TaskSite任务点
    @PostMapping("/add")
    public ResponseEntity<?> createTask(@RequestBody Task task){
        System.out.println(task.toString());
        taskService.addNewTask(task);
        return ResponseEntity.ok().body(new  MessageResponse(0,"成功创建任务！"));
    }

    // 按照日期、用户名获取巡检任务实例
    @GetMapping("/")
    public ResponseEntity<?> getTodayTaskInstanceOfSpecUser(
            @RequestParam(required = false)LocalDateTime timestamp,
            @RequestParam(required = false)Integer user_id){
        Map<String, Object> map = new HashMap<>();
        if (timestamp != null) map.put("timestamp", timestamp);
        if (user_id != null) map.put("user_id", user_id);
        List<TaskInstance> taskInstance = taskService.findTaskInstance(map);
        return ResponseEntity.ok().body(new MessageResponseBody(0, "获取任务实例成功", taskInstance));
    }
}

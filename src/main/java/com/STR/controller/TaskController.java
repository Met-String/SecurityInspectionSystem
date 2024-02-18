package com.STR.controller;

import com.STR.entity.MessageResponse;
import com.STR.entity.Task;
import com.STR.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // 创建新任务
    @PostMapping("/add")
    public ResponseEntity<?> createTask(@RequestBody Task task){
        System.out.println("咳咳");
        System.out.println(task.toString());
        taskService.addNewTask(task);
        System.out.println("咳咳2");
        return ResponseEntity.ok().body(new  MessageResponse(0,"成功创建任务！"));
    }
}

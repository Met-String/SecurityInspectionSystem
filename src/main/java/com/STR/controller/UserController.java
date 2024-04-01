package com.STR.controller;

import com.STR.entity.MessageResponseBody;
import com.STR.entity.TaskInstance;
import com.STR.entity.User;
import com.STR.service.TaskService;
import com.STR.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 负责和特定于用户的信息有关的接口，包括统计、巡检历史等
@Controller
@RequestMapping("/user")
public class UserController {
    private final TaskService taskService;
    private final UserService userService;

    public UserController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }
    // 获取用户的巡检历史
    @GetMapping("history")
    public ResponseEntity<?> historyOfUser(@RequestParam("user_id") int user_id){
        Map<String,Object> map = new HashMap<>();
        map.put("user_id",user_id);
        List<TaskInstance> taskInstances = taskService.findInstanceByCondition(map);
        return ResponseEntity.ok().body(new MessageResponseBody(0,"获取用户巡检历史成功！", taskInstances));
    }

    // 获取所有用户信息
    @GetMapping("get/all")
    public ResponseEntity<?> getAllUser(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok().body(new MessageResponseBody(0,"获取用户和列表成功！", users));
    }
}

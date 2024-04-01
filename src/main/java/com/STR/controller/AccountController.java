package com.STR.controller;


import com.STR.entity.MessageResponse;
import com.STR.entity.User;
import com.STR.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("account")
// 用于管辖和账号信息操作相关的API
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

// 用户登录，接受账号、密码、验证码。返回

    //注册接口
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // 检查电话号是否”无效”
        if (userService.unavailByPhoneNumber(user.getPhoneNumber())) {
            return ResponseEntity.badRequest().body(new MessageResponse(-1, "错误: 请正确输入电话号！"));
        }

        // 检查电话号是否重复
        if (userService.existsByPhoneNumber(user.getPhoneNumber())) {
            return ResponseEntity.badRequest().body(new MessageResponse(-1, "错误: 此号码已经存在用户!"));
        }

        // 创建新账户
        user.setPassword(encoder.encode(user.getPassword()));
//        User newUser = new User(-1,user.getUserName(), user.getPhoneNumber(), encoder.encode(user.getPassword()));
        if (userService.save(user) == 1) {
            return ResponseEntity.ok(new MessageResponse(0, "注册成功!"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse(1, "由于服务器出现未知异常，新用户创建失败！"));
        }
    }

    //登录接口
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody User user) {
        // 检查电话号是否”无效”
        if (userService.unavailByPhoneNumber(user.getPhoneNumber())) {
            return ResponseEntity.badRequest().body(new MessageResponse(-1, "错误: 请正确输入电话号！"));
        }

        if (userService.authenticateUser(user)) {
            return ResponseEntity.ok(new MessageResponse(0, "登录成功!"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse(-1, "电话号或密码错误！"));
        }
    }
}
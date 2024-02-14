package com.STR.service;

import com.STR.entity.User;

public interface UserService {
    //验证电话号码是否"无效"，若无效返回True
    Boolean unavailByPhoneNumber(String phoneNumber);

    //验证电话号码是否已经存在
    Boolean existsByPhoneNumber(String phoneNumber);

    //保存仅包含用户名、电话号、密码的新用户
    int save(User newUser);

    // 验证用户是否合法
    Boolean authenticateUser(User user);

}
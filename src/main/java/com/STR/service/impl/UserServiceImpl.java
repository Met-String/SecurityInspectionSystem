package com.STR.service.impl;

import com.STR.entity.User;
import com.STR.mapper.UserMapper;
import com.STR.service.UserService;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    //检查电话号的无效性
    @Override
    public Boolean unavailByPhoneNumber(String initPhoneNumber) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            PhoneNumber phoneNumber = phoneUtil.parse(initPhoneNumber, "CN");
            boolean isValid = phoneUtil.isValidNumber(phoneNumber); // 检查号码是否有效
            if (isValid) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
    }

    // 检查号码是否已经在数据库中存在
    @Override
    public Boolean existsByPhoneNumber(String phoneNumber) {
        User users = userMapper.findUserByPhoneNumber(phoneNumber);
        if (users == null){
            return false;
        } else {
            return true;
        }
    }

    // 保存新用户
    @Override
    public int save(User newUser) {
        return userMapper.addNewUser(newUser);
    }

    // 验证用户合法性
    @Override
    public Boolean authenticateUser(User user) {
        User matchedUser = userMapper.findUserByPhoneNumber(user.getPhoneNumber());
        if (matchedUser != null && passwordEncoder.matches(user.getPassword() ,matchedUser.getPassword())) {
            return true;
        } else {
            return false;
        }
    }

    // 根据点位ID查找所负责的用户的基本信息
    @Override
    public User findUserBySiteID(int site_id) {
        return userMapper.findUserBySiteID(site_id);
    }

    // 查找所有用户
    @Override
    public List<User> getAllUsers() {
        return userMapper.findAllUsers();
    }
}

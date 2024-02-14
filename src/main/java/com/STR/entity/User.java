package com.STR.entity;

import lombok.Data;

@Data
public class User {
    String userName;
    String phoneNumber;
    String password;

    public User(){};
    public User(String userName, String phoneNumber, String password) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
}

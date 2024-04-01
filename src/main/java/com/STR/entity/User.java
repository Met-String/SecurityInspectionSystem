package com.STR.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    int user_id;
    String userName;
    String phoneNumber;
    String password;
    String position;
    String image_url;
    String department;
}

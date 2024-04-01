package com.STR.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Site {
    int site_id;
    int organization_id;
    int group_id;
    String name;
    String address;
    double longitude;
    double latitude;
    String state;
    String usability;
    List<Integer> frequency;
    String qrcode_serial_number;
    String remark;
    LocalDateTime last_check_time;
    LocalDate next_check_date;
    // 在数据库Site表中不存在此字段，为了便利前端而设立。
    int user_id;
    // 在数据库Site表中不存在此字段，为了便利前端而设立。
    String user_name;
}

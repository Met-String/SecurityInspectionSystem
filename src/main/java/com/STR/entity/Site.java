package com.STR.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    int state;
    String qrcode_serial_number;
    String remark;
}

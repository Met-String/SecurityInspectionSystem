package com.STR.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NormalInspection {
    int normalinspection_id;
    int tasksiteinstance_id;
    String description;
    int anomaly; // 0代表无异常 1代表异常
    String image_url;
}

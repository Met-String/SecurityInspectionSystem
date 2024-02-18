package com.STR.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    int task_id;
    String name;
    int user_id;
    String description;
    LocalDateTime start_time;
    LocalDateTime end_time;
    int status;
    int organization_id;
    int creator_id;
}

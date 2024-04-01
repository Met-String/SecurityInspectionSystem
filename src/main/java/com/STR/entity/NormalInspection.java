package com.STR.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// NormalInspection的生成是在TaskSiteInspection完成时被直接插入数据库的 因此
// 此数据结构仅有 插入 查询 两种操作 其高度绑定TaskSiteInspection 不存在单独被调取的可能
// TaskSiteInspection被完成时 会根据情况插入NormalInspection 或插入工单 关于工单 为一个独立的体系
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NormalInspection {
    int normalinspection_id;
    int tasksiteinstance_id;
    String description;
    String image_url;
}

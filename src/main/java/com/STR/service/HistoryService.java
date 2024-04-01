package com.STR.service;

import com.STR.entity.TaskSiteInstance;

import java.util.List;

public interface HistoryService {
    // 查找一个点位的巡检历史
    List<TaskSiteInstance> findHistoryOfSite(int site_id);
}

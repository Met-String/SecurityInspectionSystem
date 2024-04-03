package com.STR.service.impl;

import com.STR.entity.TaskSiteInstance;
import com.STR.mapper.NormalInspectionMapper;
import com.STR.mapper.TaskSiteInstanceMapper;
import com.STR.service.HistoryService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


// 管理以 taskSiteInstance 为核心的各类巡检历史
@Service
public class HistoryServiceImpl implements HistoryService {
    private final TaskSiteInstanceMapper taskSiteInstanceMapper;
    private final NormalInspectionMapper normalInspectionMapper;

    public HistoryServiceImpl(TaskSiteInstanceMapper taskSiteInstanceMapper, NormalInspectionMapper normalInspectionMapper) {
        this.taskSiteInstanceMapper = taskSiteInstanceMapper;
        this.normalInspectionMapper = normalInspectionMapper;
    }

    // 查找一个点位的巡检历史
    @Override
    public List<TaskSiteInstance> findHistoryOfSite(int site_id) {
        Map<String,Object> map = new HashMap<>();
        map.put("site_id",site_id);
        List<TaskSiteInstance> taskSiteInstances = taskSiteInstanceMapper.findByCondition(map);
        // 分别补全每个巡检点位实例的NormalInspection记录
        for (TaskSiteInstance taskSiteInstance : taskSiteInstances){
            //补全NormalInspection属性
            taskSiteInstance.setNormalInspection(normalInspectionMapper.findByTaskSiteInstanceID(taskSiteInstance.getTasksiteinstance_id())) ;
        }
        return taskSiteInstances;
    }
}

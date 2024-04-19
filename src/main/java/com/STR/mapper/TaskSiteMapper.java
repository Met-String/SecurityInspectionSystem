package com.STR.mapper;

import com.STR.entity.TaskSite;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface TaskSiteMapper {
    int insertTaskSite(TaskSite taskSite);
    Set<Integer> findSiteIDListByTaskID(int task_id);
    int deleteByTaskID(int task_id);
    int deleteBySiteID(int site_id);
}

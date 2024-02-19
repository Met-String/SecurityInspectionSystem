package com.STR.mapper;

import com.STR.entity.Task;
import com.STR.entity.TaskSiteInstance;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskSiteInstanceMapper {
    int insertTaskSiteInstance(TaskSiteInstance taskSiteInstance);
}

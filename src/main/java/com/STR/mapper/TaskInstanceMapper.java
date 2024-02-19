package com.STR.mapper;

import com.STR.entity.Task;
import com.STR.entity.TaskInstance;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskInstanceMapper {
    int insertTaskInstance(TaskInstance taskInstance);
}

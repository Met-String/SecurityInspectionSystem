package com.STR.mapper;

import com.STR.entity.TaskInstance;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TaskInstanceMapper {
    int insertTaskInstance(TaskInstance taskInstance);
    List<TaskInstance> findTaskInstance(Map<String,Object> map);
}

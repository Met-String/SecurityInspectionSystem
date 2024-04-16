package com.STR.mapper;

import com.STR.entity.Task;
import com.STR.entity.TaskInstance;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TaskInstanceMapper {
    int insertTaskInstance(TaskInstance taskInstance);
    int update(TaskInstance taskInstance);
    List<TaskInstance> findByCondition(Map<String,Object> map);

    int deleteByID(int taskinstance_id);
}

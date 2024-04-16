package com.STR.mapper;


import com.STR.entity.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TaskMapper {
    int insertTask(Task task);
    int deleteByTaskID(int task_id);
    List<Task> selectByCondition(Map<String,Object> map);
    int update(Task task);
}

package com.STR.mapper;


import com.STR.entity.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TaskMapper {
    int insertTask(Task task);

    List<Task> selectByCondition(Map<String,Object> map);
}

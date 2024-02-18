package com.STR.mapper;


import com.STR.entity.Task;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskMapper {
    int insertTask(Task task);
}

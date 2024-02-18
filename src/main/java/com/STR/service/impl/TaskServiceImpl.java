package com.STR.service.impl;


import com.STR.entity.Task;
import com.STR.mapper.TaskMapper;
import com.STR.service.TaskService;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Override
    public int addNewTask(Task task) {
        return taskMapper.insertTask(task);
    }
}

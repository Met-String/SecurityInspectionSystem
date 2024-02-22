package com.STR.mapper;

import com.STR.entity.TaskSiteInstance;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;


@Mapper
public interface TaskSiteInstanceMapper {
    int insertTaskSiteInstance(TaskSiteInstance taskSiteInstance);
    List<TaskSiteInstance> findTaskSiteInstanceByTaskInstanceID(int taskinstance_id);

    List<TaskSiteInstance> findTaskSiteInstanceBySiteID(int site_id);

    int updateTaskSiteInstance(TaskSiteInstance taskSiteInstance);
}

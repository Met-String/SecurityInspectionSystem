package com.STR.mapper;

import com.STR.entity.TaskSiteInstance;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;


@Mapper
public interface TaskSiteInstanceMapper {
    int insert(TaskSiteInstance taskSiteInstance);

    // 根据条件查找巡检记录
    List<TaskSiteInstance> findByCondition(Map<String,Object> map);
    List<TaskSiteInstance> findByTaskInstanceID(int taskinstance_id);

    List<TaskSiteInstance> findBySiteID(int site_id);

    // 找到某个用户的巡检记录
    List<TaskSiteInstance> findByUserID(int user_id);
    TaskSiteInstance findByID(int tasksiteinstance_id);

    int update(TaskSiteInstance taskSiteInstance);

    int countTimesByInterval(int interval);
}

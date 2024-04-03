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
    int update(TaskSiteInstance taskSiteInstance);

    int countTimesByInterval(int interval);
}

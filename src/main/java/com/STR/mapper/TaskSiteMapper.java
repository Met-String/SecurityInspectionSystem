package com.STR.mapper;

import com.STR.entity.TaskSite;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskSiteMapper {
    int insertTaskSite(TaskSite taskSite);
}

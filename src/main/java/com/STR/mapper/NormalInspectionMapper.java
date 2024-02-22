package com.STR.mapper;

import com.STR.entity.NormalInspection;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NormalInspectionMapper {
    int insertNormalInspection(NormalInspection normalInspection);
    NormalInspection findNormalInspectionByTaskSiteInstanceID(int taskSiteInstance_id);
}

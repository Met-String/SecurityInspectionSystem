package com.STR.mapper;

import com.STR.entity.Site;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SiteMapper {
    int addNewSite(Site site);
    int editSiteById(Site site);
    int deleteSiteByID(int id);
    List<Site> selectSitesByOrganizationID(int organization_id);
}

package com.STR.service;

import com.STR.entity.Site;

import java.util.List;

public interface SiteService {
    // 添加新点位
    int addNewSite(Site site);
    // 编辑点位
    int editSiteById(Site site);
    // 删除点位
    int deleteSiteByID(int site);

    // 根据项目ID获取所有点位
    List<Site> selectSitesByOrganizationID(int organization_id);
}

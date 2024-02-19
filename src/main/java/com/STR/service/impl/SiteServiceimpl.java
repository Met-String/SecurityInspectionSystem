package com.STR.service.impl;

import com.STR.entity.Site;
import com.STR.mapper.SiteMapper;
import com.STR.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteServiceimpl implements SiteService {
    private final SiteMapper siteMapper;

    @Autowired
    public SiteServiceimpl(SiteMapper siteMapper) {
        this.siteMapper = siteMapper;
    }

    // 添加新点位
    @Override
    public void addNewSite(Site site) {
        siteMapper.addNewSite(site);
    }

    // 编辑现有点位
    @Override
    public void editSiteById(Site site) {
        siteMapper.editSiteById(site);
    }

    // 删除点位
    @Override
    public void deleteSiteByID(int id) {
        siteMapper.deleteSiteByID(id);
    }

    // 根据项目ID查找所有点位
    @Override
    public List<Site> selectSitesByOrganizationID(int organization_id) {
        return siteMapper.selectSitesByOrganizationID(organization_id);
    }
}

package com.STR.service.impl;

import com.STR.entity.Site;
import com.STR.entity.TaskSiteInstance;
import com.STR.mapper.SiteMapper;
import com.STR.mapper.TaskMapper;
import com.STR.mapper.TaskSiteInstanceMapper;
import com.STR.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteServiceimpl implements SiteService {
    private final SiteMapper siteMapper;
    private final TaskSiteInstanceMapper taskSiteInstanceMapper;

    @Autowired
    public SiteServiceimpl(SiteMapper siteMapper, TaskMapper taskMapper, TaskSiteInstanceMapper taskSiteInstanceMapper) {
        this.siteMapper = siteMapper;
        this.taskSiteInstanceMapper = taskSiteInstanceMapper;
    }

    // 添加新点位
    @Override
    public Boolean addNewSite(Site site) {
        if (siteMapper.addNewSite(site) == 1){
            return true;
        }else {
            return false;
        }
    }

    // 编辑现有点位
    @Override
    public void editSiteById(Site site) {
        siteMapper.editSiteById(site);
    }

    // 删除ID列表中的所有点位
    @Override
    public void deleteSitesByID(List<Integer> sitesIdList) {
        for(int site_id : sitesIdList){
            siteMapper.deleteSiteByID(site_id);
        }
    }

    // 根据项目ID查找所有点位
    @Override
    public List<Site> selectSitesByOrganizationID(int organization_id) {
        return siteMapper.selectSitesByOrganizationID(organization_id);
    }



}

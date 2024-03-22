package com.STR.service.impl;

import com.STR.entity.Site;
import com.STR.entity.User;
import com.STR.mapper.SiteMapper;
import com.STR.mapper.TaskMapper;
import com.STR.mapper.TaskSiteInstanceMapper;
import com.STR.service.SiteService;
import com.STR.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteServiceimpl implements SiteService {
    private final SiteMapper siteMapper;
    private final TaskSiteInstanceMapper taskSiteInstanceMapper;
    private final UserService userService;
    @Autowired
    public SiteServiceimpl(SiteMapper siteMapper, TaskMapper taskMapper, TaskSiteInstanceMapper taskSiteInstanceMapper, UserService userService) {
        this.siteMapper = siteMapper;
        this.taskSiteInstanceMapper = taskSiteInstanceMapper;
        this.userService = userService;
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
        List<Site> result= siteMapper.selectSitesByOrganizationID(organization_id);
        for(Site site: result){
            User user = userService.findUserBySiteID(site.getSite_id());
            //一个点位有可能暂时没有负责人
            if(user != null) {
                site.setUser_id(user.getUser_id());
                site.setUser_name(user.getUserName());
            }
        }
        return result;
    }



}

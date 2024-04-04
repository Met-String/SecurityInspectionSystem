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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // 查找所有点位
    @Override
    public List<Site> findSitesByCondition(Map<String,Object> map) {
        List<Site> siteList = siteMapper.selectByCondition(map);
        return siteList;
    }

    @Override
    public List<Site> selectByTaskID(int task_id) {
        Map<String,Object> map = new HashMap<>();
        map.put("task_id",task_id);
        List<Site> sitePool = siteMapper.selectByCondition(map);
        return sitePool;
    }
}

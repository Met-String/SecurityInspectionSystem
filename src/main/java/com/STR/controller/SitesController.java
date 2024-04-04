package com.STR.controller;

import com.STR.entity.MessageResponse;
import com.STR.entity.MessageResponseBody;
import com.STR.entity.Site;
import com.STR.service.HistoryService;
import com.STR.service.SiteService;
import com.STR.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("sites")
// 用于管辖点位相关设置的API
public class SitesController {
    private final SiteService siteService;
    private final TaskService taskService;
    private final HistoryService historyService;

    public SitesController(SiteService siteService, TaskService taskService, HistoryService historyService) {
        this.siteService = siteService;
        this.taskService = taskService;
        this.historyService = historyService;
    }

    // 添加新点位
    @PostMapping("/add")
    public ResponseEntity<?> addNewSite(@RequestBody Site site){
        if (siteService.addNewSite(site))
            return ResponseEntity.ok().body(new MessageResponse(0,"添加点位成功！"));
        else
            return ResponseEntity.ok().body(new MessageResponse(-1,"由于某些原因,点位添加失败！"));
    }

    // 编辑点位
    @PostMapping("/edit")
    public ResponseEntity<?> editSite(@RequestBody Site site){
        siteService.editSiteById(site);
        return ResponseEntity.ok().body(new MessageResponse(0,"编辑点位成功"));
    }

    // 删除点位
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteSite(@RequestBody List<Integer> sitesIdList){
        siteService.deleteSitesByID(sitesIdList);
        return ResponseEntity.ok().body(new MessageResponse(0,"删除成功！"));
    }

    // 条件查找点位
    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<?> getSites(){
        Map<String,Object> map = new HashMap<>();
        return ResponseEntity.ok().body(siteService.findSitesByCondition(map));
    }

    // 查找一个点位的所有历史巡检记录，也许是
    @GetMapping("/history")
    @ResponseBody
    public ResponseEntity<?> findSiteInspectHistory(@RequestParam("site_id") int site_id){
        return ResponseEntity.ok().body(new MessageResponseBody(0,"获取巡查历史成功！",historyService.findHistoryOfSite(site_id)));
    }

}

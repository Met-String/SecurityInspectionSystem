package com.STR.controller;

import com.STR.entity.MessageResponse;
import com.STR.entity.MessageResponseBody;
import com.STR.entity.Site;
import com.STR.service.SiteService;
import com.STR.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("sites")
// 用于管辖点位相关设置的API
public class SitesController {
    private final SiteService siteService;
    private final TaskService taskService;

    public SitesController(SiteService siteService, TaskService taskService) {
        this.siteService = siteService;
        this.taskService = taskService;
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

    //删除点位
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteSite(@RequestParam("id") int id){
        siteService.deleteSiteByID(id);
        return ResponseEntity.ok().body(new MessageResponse(0,"删除成功"));
    }

    // 根据项目ID查找所有点位（这个或许只是测试，实际产品中应该随着项目各种数据一起被返回了）
    @GetMapping("/allSitesOfOrg")
    @ResponseBody
    public ResponseEntity<?> allSitesOfOrgID(@RequestParam("organization_id") int organization_id){
        siteService.selectSitesByOrganizationID(organization_id);
        return ResponseEntity.ok().body(siteService.selectSitesByOrganizationID(organization_id));
    }

    // 查找一个点位的所有历史巡检记录，也许是
    @GetMapping("/history")
    @ResponseBody
    public ResponseEntity<?> findSiteInspectHistory(@RequestParam("site_id") int site_id){
        return ResponseEntity.ok().body(new MessageResponseBody(0,"获取巡查历史成功！",taskService.findHistoryOfSite(site_id)));
    }

}

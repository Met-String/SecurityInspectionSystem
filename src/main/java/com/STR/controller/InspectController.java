package com.STR.controller;

import com.STR.entity.MessageResponse;
import com.STR.entity.TaskSiteInstance;
import com.STR.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("inspect")
// 用于管辖巡检任务进程相关的API，
public class InspectController {
    private final TaskService taskService;

    public InspectController(TaskService taskService) {
        this.taskService = taskService;
    }

    //完成该点位的巡检任务，注意传入体必须要有TaskSiteInstanceID、CheckTime、NormalInspection三项。
    //其中NormalInspection中必须包含description、anomaly、imageUrl三项填写
    @PostMapping("/siteInstance/finish")
    ResponseEntity<?> siteInstanceFinish(@RequestBody TaskSiteInstance taskSiteInstance){
        if(taskService.finishTaskSiteInstance(taskSiteInstance) == 1){
            return ResponseEntity.ok().body(new MessageResponse(-1,"请勿重复提交任务！"));
        }
        return ResponseEntity.ok().body(new MessageResponse(0,"点位巡检任务完成！"));
    }
}

package com.STR.controller;

import com.STR.entity.MessageResponse;
import com.STR.entity.TaskSiteInstance;
import com.STR.service.TaskService;
import com.STR.util.ImageUploadUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("inspect")
// 用于管辖巡检任务进程相关的API
public class InspectController {
    private final TaskService taskService;
    private final ImageUploadUtil imageUploadUtil;

    public InspectController(TaskService taskService, ImageUploadUtil imageUploadUtil) {
        this.taskService = taskService;
        this.imageUploadUtil = imageUploadUtil;
    }

    //完成该点位的巡检任务，注意传入体必须要有TaskSiteInstanceID、CheckTime、NormalInspection三项。
    //其中NormalInspection中必须包含description、anomaly、imageUrl三项填写
    @PostMapping("/siteInstance/finish")
    ResponseEntity<?> siteInstanceFinish(@RequestParam("taskSiteInstance") String taskSiteInstance,
                                         @RequestParam(value = "img", required = false) MultipartFile img){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        // 确保日期和时间类型不被序列化为时间戳
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            TaskSiteInstance taskSiteInstance1 = objectMapper.readValue(taskSiteInstance,TaskSiteInstance.class);
            if(taskService.finishTaskSiteInstance(taskSiteInstance1, img) == 1){
                return ResponseEntity.ok().body(new MessageResponse(-1,"请勿重复提交任务！"));
            }
            System.out.println(taskSiteInstance.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(new MessageResponse(0,"点位巡检任务完成！"));
    }
}

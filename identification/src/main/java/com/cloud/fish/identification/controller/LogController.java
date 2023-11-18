package com.cloud.fish.identification.controller;

import com.cloud.fish.identification.model.entity.Log;
import com.cloud.fish.identification.service.LogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 操作日志
 */
@RestController
public class LogController {

    @Resource
    private LogService logService;

    @GetMapping("/fish/getLog")
    public List<Log> getLog() {
        return logService.list();
    }
}

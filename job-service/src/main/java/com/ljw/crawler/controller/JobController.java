package com.ljw.crawler.controller;

import com.ljw.crawler.core.AjaxResult;
import com.ljw.crawler.service.JobServiceStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: langjianwei
 * @DateTime: 2021/8/20 16:45
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobServiceStrategy jobServiceStrategy;

    @RequestMapping("/test/{type}")
    public AjaxResult test1(@PathVariable("type") String type) {
        return AjaxResult.success(jobServiceStrategy.buildTask(type));
    }
}

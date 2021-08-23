package com.ljw.crawler.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class JobServiceStrategy {

    HashMap<String, JobService> map = new HashMap<>(8);

    /**
     * @Author: 郎建伟
     * @Description: 构造函数，会把spring容器中所有关于该接口的子类，全部放入到集合中
     * @Date: Created in 2020/9/9 14:09
     * @param: [jobServices]
     */
    public JobServiceStrategy(List<JobService> jobServices) {
        for (JobService jobService : jobServices) {
            map.put(jobService.getType(), jobService);
        }
    }

    public Object buildTask(String type){
        JobService service = map.get(type);
        if (null == service) {
            return "没有对应的业务类型：" + type;
        }
        return service.getSkus();
    }
}

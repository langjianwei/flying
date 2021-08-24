package com.ljw.crawler.service.impl;

import com.ljw.crawler.datasource.DataSourcesAop;
import com.ljw.crawler.entity.Skus;
import com.ljw.crawler.mapper.JobMapper;
import com.ljw.crawler.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobServiceWynImpl implements JobService {

    @Autowired
    private JobMapper jobMapper;

    @Override
    public String getType() {
        return "wyn";
    }

    @DataSourcesAop(value = "default-db")
    public List<Skus> getSkus(){
        return jobMapper.getSkus();
    }

}

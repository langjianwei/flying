package com.ljw.crawler.service;


import com.ljw.crawler.entity.Skus;

import java.util.List;

public interface JobService {

    String getType();

    List<Skus> getSkus();
}

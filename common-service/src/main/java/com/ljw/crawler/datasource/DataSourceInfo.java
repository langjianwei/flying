package com.ljw.crawler.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 郎建伟
 * @Description: 读取配置文件的数据源信息
 * @Date: Created in 2020/9/9 13:52
 */
@Component
@ConfigurationProperties(prefix = "datasources")
public class DataSourceInfo {

    private String defaultDb;
    private List<CustomDataSource> list = new ArrayList<>();

    public String getDefaultDb() {
        return defaultDb;
    }

    public DataSourceInfo setDefaultDb(String defaultDb) {
        this.defaultDb = defaultDb;
        return this;
    }

    public List<CustomDataSource> getList() {
        return list;
    }

    public DataSourceInfo setList(List<CustomDataSource> list) {
        this.list = list;
        return this;
    }
}

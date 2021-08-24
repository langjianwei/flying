package com.ljw.crawler.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 郎建伟
 * @Description: 读取配置文件的数据源信息
 * @Date: Created in 2020/9/9 13:52
 */
@ConfigurationProperties(prefix = "datasources")
public class DataSourcesProperties {

    private String defaultDb;
    private List<DataSourcesInfo> list = new ArrayList<>();

    public String getDefaultDb() {
        return defaultDb;
    }

    public DataSourcesProperties setDefaultDb(String defaultDb) {
        this.defaultDb = defaultDb;
        return this;
    }

    public List<DataSourcesInfo> getList() {
        return list;
    }

    public DataSourcesProperties setList(List<DataSourcesInfo> list) {
        this.list = list;
        return this;
    }
}

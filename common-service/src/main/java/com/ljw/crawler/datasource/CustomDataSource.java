package com.ljw.crawler.datasource;


/**
 * @Author: 郎建伟
 * @Description: 自定义数据源bean，用于映射配置文件数据源信息
 * @Date: Created in 2020/9/9 13:53
 */
public class CustomDataSource {

    private String dataSourceName;
    private Boolean enable;
    private String driverClassName;
    private String url;
    private String username;
    private String password;

    public String getDataSourceName() {
        return dataSourceName;
    }

    public CustomDataSource setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
        return this;
    }

    public Boolean getEnable() {
        return enable;
    }

    public CustomDataSource setEnable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public CustomDataSource setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public CustomDataSource setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public CustomDataSource setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public CustomDataSource setPassword(String password) {
        this.password = password;
        return this;
    }
}

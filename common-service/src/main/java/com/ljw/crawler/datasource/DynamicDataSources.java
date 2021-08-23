package com.ljw.crawler.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @Author: 郎建伟
 * @Description: 动态数据源（需要继承AbstractRoutingDataSource）,
 *               在访问数据库时会调用该类的determineCurrentLookupKey()方法获取数据库实例的 key
 * @Date: Created in 2020/9/9 14:08
 */
public class DynamicDataSources extends AbstractRoutingDataSource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static volatile DynamicDataSources dynamicDataSources;

    /**
     * 采用单例模式获取DynamicDataSource对象实例
     * DCL双重检查锁机制(或者静态内部类)保证多线程不安全问题和效率问题
     */
    public static DynamicDataSources getInstance() {
        if (null == dynamicDataSources) {
            synchronized (DynamicDataSources.class) {
                if (null == dynamicDataSources) {
                    dynamicDataSources = new DynamicDataSources();
                }
            }
        }
        return dynamicDataSources;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceName = DataSourcesContextHolder.getDataSourceType();
        if (null == dataSourceName) {
            logger.info("未设置数据源，使用默认数据源: [{}]", "db_default");
        } else {
            logger.info("当前数据源为: [{}]", dataSourceName);
        }
        return dataSourceName;
    }

}

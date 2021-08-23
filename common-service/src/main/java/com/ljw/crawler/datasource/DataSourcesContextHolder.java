package com.ljw.crawler.datasource;

/**
 * @Author: 郎建伟
 * @Description: 保存一个线程安全的DataSource容器
 * @Date: Created in 2020/9/9 14:06
 */
public class DataSourcesContextHolder {

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 获取数据源
     * @return
     */
    public static String getDataSourceType() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 切换数据源
     * @param key
     */
    public static void setDataSourceType(String key) {
        CONTEXT_HOLDER.set(key);
    }

    /**
     * 清理数据源
     */
    public static void cleanDataSourceType() {
        CONTEXT_HOLDER.remove();
    }
}

package com.ljw.crawler.datasource;

/**
 * @Author: 郎建伟
 * @Description: 保存一个线程安全的DataSource容器
 * @Date: Created in 2020/9/9 14:06
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    public static String getDataSourceType() {
        return CONTEXT_HOLDER.get();
    }

    public static void setDataSourceType(String key) {
        CONTEXT_HOLDER.set(key);
    }

    public static void cleanDataSourceType() {
        CONTEXT_HOLDER.remove();
    }
}

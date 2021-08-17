package com.ljw.crawler.parse;

import java.util.List;

/**
 * @Description: 统一的解析器
 * @Author: langjianwei
 * @DateTime: 2021/6/9 15:45
 * @Version: 1.0.0
 */
public interface Parse {

    /**
     * 抽取单个返回结果，如果有多个结果，只返回第一个
     * @param text
     * @param path
     * @param <T>
     * @return
     */
    <T> T parse(String text, String path);

    /**
     * 抽取所有返回结果集
     * @param text
     * @param path
     * @param <T>
     * @return
     */
    <T> List<T> parseList(String text, String path);
}

package com.ljw.flying.downloader;

import java.util.Map;

/**
 * @Description: 
 * @Author: langjianwei
 * @DateTime: 2021/6/1 17:51
 * @Version: 1.0.0
 */
public interface Downloader {

    /**
     * 下载目标资源
     * @param url
     * @return
     */
    String down(String url);

    /**
     * 下载目标资源
     * @param url
     * @param params
     * @return
     */
    String down(String url, Map<String, Object> params);

    /**
     * 下载目标资源
     * @param url
     * @param params
     * @param headers
     * @return
     */
    String down(String url, Map<String, Object> params, Map<String, Object> headers);
}

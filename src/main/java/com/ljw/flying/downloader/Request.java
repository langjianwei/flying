package com.ljw.flying.downloader;

import lombok.Data;

import java.util.Map;

/**
 * @Description: 请求配置（参数，header，代理...）
 * @Author: langjianwei
 * @DateTime: 2021/6/1 18:42
 * @Version: 1.0.0
 */
@Data
public class Request {

    private String url;
    private Map<String, String> headers;
    private Map<String, Object> params;
    private Long timeout;
}

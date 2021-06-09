package com.ljw.flying.fetch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @Description: 请求配置（参数，header，代理...）
 * @Author: langjianwei
 * @DateTime: 2021/6/1 18:42
 * @Version: 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Request {

    private String url;
    private String method;
    private Map<String, Object> params;
    private Map<String, Object> headers;
    private Long timeout;
    private Proxy proxy;

    public static Request custom() {
        return new Request();
    }


    /**
     * 请求方法类型
     */
    enum Method {
        GET, POST
    }
}

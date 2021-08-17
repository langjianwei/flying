package com.ljw.crawler.fetch;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;

/**
 * @Description: 通用的下载接口
 * @Author: langjianwei
 * @DateTime: 2021/6/1 17:51
 * @Version: 1.0.0
 */
public interface Fetch {

    /**
     * 请求
     * @param url
     * @return
     */
    String down(String url);

    /**
     * 请求
     * @param url
     * @param method
     * @return
     */
    String down(String url, String method);

    /**
     * 请求
     * @param url
     * @param params
     * @return
     */
    String down(String url, Map<String, Object> params);

    /**
     * 请求
     * @param url
     * @param method
     * @param params
     * @return
     */
    String down(String url, String method, Map<String, Object> params);

    /**
     * 请求
     * @param url
     * @param params
     * @param headers
     * @return
     */
    String down(String url, Map<String, Object> params, Map<String, Object> headers);

    /**
     * 请求
     * @param url
     * @param params
     * @param method
     * @param headers
     * @return
     */
    String down(String url, String method, Map<String, Object> params, Map<String, Object> headers);

    /**
     * 请求
     * @param request
     * @return
     */
    String down(Request request);

    /**
     * 异步请求
     * @param request
     * @param callBack，异步回调
     * @return
     */
    void down(Request request, Consumer<String> callBack);


    /**
     * 按照指定字符集解压缩数据
     * @param data
     * @param charset
     * @return
     */
    default String gunZip(byte[] data, Charset charset) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new ByteArrayInputStream(data)), charset));
            StringBuffer sb = new StringBuffer();
            char[] chars = new char[4096];
            while(br.ready()) {
                int len = br.read(chars);
                if(len > 0) {
                    sb.append(chars, 0, len);
                }
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

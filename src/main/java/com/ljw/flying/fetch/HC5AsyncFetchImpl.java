package com.ljw.flying.fetch;

import com.ljw.flying.utils.net.AllCertsTrustManager;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequests;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicHttpRequest;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.net.WWWFormCodec;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.Timeout;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.Closeable;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * @Description: httpclient5异步的下载器实现类
 * @Author: langjianwei
 * @DateTime: 2021/6/1 18:51
 * @Version: 1.0.0
 */
public class HC5AsyncFetchImpl implements Fetch, Closeable {

    private final CloseableHttpAsyncClient client;
    private final Charset charset;
    private final long timeout;

    /**
     *
     * @param timeout，默认毫秒
     */
    public HC5AsyncFetchImpl(long timeout) {
        this(timeout, StandardCharsets.UTF_8);
    }

    /**
     *
     * @param timeout，默认毫秒
     * @param charset，默认utf-8
     */
    public HC5AsyncFetchImpl(long timeout, Charset charset) {
        this(initClient(timeout, true), timeout, charset);
    }

    /**
     *
     * @param client
     * @param timeout，默认毫秒
     * @param charset，默认utf-8
     */
    public HC5AsyncFetchImpl(CloseableHttpAsyncClient client, long timeout, Charset charset) {
        if(charset == null) {
            throw new IllegalArgumentException("charset is null");
        }
        if(client == null) {
            throw new IllegalArgumentException("client is null");
        }
        this.charset = charset;
        this.timeout = timeout;
        this.client = client;
        this.client.start();
    }

    @Override
    public String down(String url) {
        return this.down(url, "GET");
    }

    @Override
    public String down(String url, String method) {
        return this.down(Request.custom().setUrl(url).setMethod(method));
    }

    @Override
    public String down(String url, Map<String, Object> params) {
        return this.down(url, "GET", params);
    }

    @Override
    public String down(String url, String method, Map<String, Object> params) {
        return this.down(Request.custom().setUrl(url).setMethod(method).setParams(params));
    }

    @Override
    public String down(String url, Map<String, Object> params, Map<String, Object> headers) {
        return this.down(url, "GET", params, headers);
    }

    @Override
    public String down(String url, String method, Map<String, Object> params, Map<String, Object> headers) {
        return this.down(Request.custom().setUrl(url).setMethod(method).setParams(params).setHeaders(headers));
    }

    @Override
    public String down(Request request) {
        return this.doDown(request);
    }

    @Override
    public void down(Request request, Consumer<String> callBack) {
        this.doDown(request, callBack);
    }

    private String doDown(Request request) {
        try {
            SimpleHttpRequest simpleHttpRequest =
                    Request.Method.GET.equals(request.getMethod()) ? preGet(request) : prePost(request);
            Future<SimpleHttpResponse> response = this.client.execute(simpleHttpRequest, null);
            return this.getBody(response.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void doDown(Request request, Consumer<String> callBack) {
        try {
            SimpleHttpRequest _req = this.preGet(request);
            this.client.execute(_req, new FutureCallback<>() {
                @Override
                public void completed(SimpleHttpResponse result) {
                    callBack.accept(getBody(result));
                }

                @Override
                public void failed(Exception ex) {
                    new RuntimeException(ex);
                }

                @Override
                public void cancelled() {
                }
            });
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getBody(SimpleHttpResponse simpleHttpResponse) {
        return this.getBody(simpleHttpResponse, this.charset);
    }

    private String getBody(SimpleHttpResponse simpleHttpResponse, Charset charset) {
        boolean gzip = this.isGZip(simpleHttpResponse);
        Charset cs = this.detectCharset(simpleHttpResponse, charset);
        return gzip ? this.gunZip(simpleHttpResponse.getBodyBytes(), cs) : new String(simpleHttpResponse.getBodyBytes(), cs);
    }

    /**
     * get请求前做的一些准备
     * @param request
     * @return
     */
    private SimpleHttpRequest preGet(Request request) throws URISyntaxException {

        URIBuilder builder = new URIBuilder(request.getUrl()).setCharset(this.charset);
        // 添加请求参数
        List<NameValuePair> nameValuePairs = this.buildParams(request.getParams());
        if(nameValuePairs != null) {
            builder.setParameters(nameValuePairs);
        }
        SimpleHttpRequest get = SimpleHttpRequests.get(builder.build());
        this.setRequestConfig(get, request);
        return get;
    }

    /**
     * post请求前做的一些准备
     * @param request
     * @return
     */
    private SimpleHttpRequest prePost(Request request) {
        SimpleHttpRequest post = SimpleHttpRequests.post(request.getUrl());
        this.setRequestConfig(post, request);
        List<NameValuePair> nameValuePairs = this.buildParams(request.getParams());
        if(nameValuePairs != null) {
            String body = WWWFormCodec.format(nameValuePairs, this.charset);
            post.setBody(body, ContentType.APPLICATION_FORM_URLENCODED.withCharset(this.charset));
        }
        return post;
    }

    /**
     * 请求配置
     * @param simpleHttpRequest
     * @param reqConfig
     */
    private void setRequestConfig(SimpleHttpRequest simpleHttpRequest, Request reqConfig) {
        this.buildHeaders(simpleHttpRequest, reqConfig.getHeaders());
        if(reqConfig.getProxy() == null && reqConfig.getTimeout() == null) {
            return;
        }
        long timeout = reqConfig.getTimeout() == null ? this.timeout : reqConfig.getTimeout();
        RequestConfig.Builder builder = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(timeout))
                .setConnectTimeout(Timeout.ofMilliseconds(timeout))
                .setResponseTimeout(Timeout.ofMilliseconds(timeout))
                .setRedirectsEnabled(false)
                .setAuthenticationEnabled(false);
        this.setProxy(simpleHttpRequest, builder, reqConfig.getProxy());
        // 替换默认的请求配置
        simpleHttpRequest.setConfig(builder.build());
    }

    private void setProxy(SimpleHttpRequest simpleHttpRequest, RequestConfig.Builder builder, Proxy proxy) {
        if (null != proxy) {
            builder.setProxy(new HttpHost(proxy.getIp(), proxy.getPort()));
        }
    }

    /**
     * 构建请求headers
     * @param basicHttpRequest
     * @param headers
     */
    private void buildHeaders(BasicHttpRequest basicHttpRequest, Map<String, Object> headers) {
        if (null == basicHttpRequest) {
            return;
        }
        if (null == headers) {
            return;
        }
        headers.forEach(basicHttpRequest::setHeader);
    }

    /**
     * 构建请求参数
     * @param params
     * @return
     */
    private List<NameValuePair> buildParams(Map<String, Object> params) {
        if (null == params) {
            return null;
        }
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        params.forEach((k,v) -> {
            if(v == null) {
                return;
            }
            nameValuePairs.add(new BasicNameValuePair(k, v.toString()));
        });
        return nameValuePairs;
    }

    /**
     * 自动检测响应字符集
     * @param rsp
     * @param defaultCharset 如果检测不到用默认的
     * @return charset
     */
    protected Charset detectCharset(SimpleHttpResponse rsp, Charset defaultCharset) {
        ContentType type = rsp.getContentType();
        Charset charsetInRsp = type == null ? null : type.getCharset();
        if(charsetInRsp != null) {
            return charsetInRsp;
        }
        byte[] data = rsp.getBodyBytes();
        Charset detectCharset = CharsetDetector.INSTANCE.detect(data);
        return detectCharset == null ? defaultCharset : detectCharset;
    }

    /**
     * 是否支持解压缩
     * @param rsp
     * @return
     */
    protected boolean isGZip(SimpleHttpResponse rsp) {
        Header header = rsp.getCondensedHeader("Content-Encoding");
        return header == null ? false : "gzip".equalsIgnoreCase(header.getValue());
    }

    /**
     *
     * @param timeout，超时时间
     * @param redirect，是否重定向到响应url
     * @return CloseableHttpAsyncClient
     */
    public static CloseableHttpAsyncClient initClient(long timeout, boolean redirect) {
        return initClient(timeout, timeout, timeout,
                PoolingAsyncClientConnectionManager.DEFAULT_MAX_CONNECTIONS_PER_ROUTE,
                PoolingAsyncClientConnectionManager.DEFAULT_MAX_TOTAL_CONNECTIONS, redirect);
    }

    /**
     *
     * @param timeout，超时时间
     * @param maxConnectionsPerRoute，每个路由的最大连接
     * @param maxConnections，连接总数
     * @return CloseableHttpAsyncClient
     */
    public static CloseableHttpAsyncClient initClient(long timeout, int maxConnectionsPerRoute, int maxConnections) {
        return initClient(timeout, timeout, timeout, maxConnectionsPerRoute, maxConnections);
    }

    /**
     *
     * @param requestTimeout，从连接池获取连接的超时
     * @param connectTimeout，连接超时，即三次握手
     * @param responseTimeout，响应超时
     * @param maxConnectionsPerRoute，每个路由的最大连接
     * @param maxConnections，连接总数
     * @return CloseableHttpAsyncClient
     */
    public static CloseableHttpAsyncClient initClient(long requestTimeout, long connectTimeout, long responseTimeout, int maxConnectionsPerRoute, int maxConnections) {
        return initClient(requestTimeout, connectTimeout, responseTimeout, maxConnectionsPerRoute, maxConnections, false);
    }

    /**
     *
     * @param requestTimeout，从连接池获取连接的超时
     * @param connectTimeout，连接超时，即三次握手
     * @param responseTimeout，响应超时
     * @param maxConnectionsPerRoute，每个路由的最大连接
     * @param maxConnections，连接总数
     * @param redirect，是否重定向到响应url
     * @return
     */
    public static CloseableHttpAsyncClient initClient(long requestTimeout, long connectTimeout, long responseTimeout, int maxConnectionsPerRoute, int maxConnections, boolean redirect) {
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[] {AllCertsTrustManager.INSTANCE}, null);

            RequestConfig rc = RequestConfig.custom()
                    .setConnectionRequestTimeout(Timeout.ofMilliseconds(requestTimeout))
                    .setConnectTimeout(Timeout.ofMilliseconds(connectTimeout))
                    .setResponseTimeout(Timeout.ofMilliseconds(responseTimeout))
                    .setRedirectsEnabled(redirect)
                    .setAuthenticationEnabled(false)
                    .build();
            IOReactorConfig iorc = IOReactorConfig.custom()
                    .setSoTimeout(Timeout.ofMilliseconds(connectTimeout))
                    .build();
            TlsStrategy tlsStrategy = ClientTlsStrategyBuilder
                    .create()
                    .setSslContext(sc)
                    .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();
            PoolingAsyncClientConnectionManager cm = PoolingAsyncClientConnectionManagerBuilder
                    .create()
                    .setMaxConnTotal(maxConnections)
                    .setMaxConnPerRoute(maxConnectionsPerRoute)
                    .setTlsStrategy(tlsStrategy)
                    .build();
            return  HttpAsyncClients.custom()
                    .setConnectionManager(cm)
                    .setDefaultRequestConfig(rc)
                    .setIOReactorConfig(iorc)
                    .disableAuthCaching()
                    .disableCookieManagement()
                    .disableConnectionState()
                    .build();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close(){
        try {
            this.client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

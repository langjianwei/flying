package com.ljw.crawler;

import com.ljw.crawler.fetch.HC5AsyncFetchImpl;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class CrawlerServiceApplicationTests {


    @Test
    public void testHC5Get() {
        HC5AsyncFetchImpl async = new HC5AsyncFetchImpl(10000, Charset.defaultCharset());
        String html = async.down("http://idea.94goo.com/key");
        System.out.println(Jsoup.parse(html).text());
    }
    @Test
    public void testHC5Post() {
        HC5AsyncFetchImpl async = new HC5AsyncFetchImpl(10000, Charset.defaultCharset());
        Map<String ,Object> params = new HashMap<>();
        params.put("doctitle","习声回响");
        params.put("page","2");
        params.put("size",10);
        Map<String ,Object> headers = new HashMap<>();
        headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36");
        String html = async.down("http://s.cnr.cn/search", "POST", params, headers);
        System.out.println(Jsoup.parse(html).text());
    }

}

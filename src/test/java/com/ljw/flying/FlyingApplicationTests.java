package com.ljw.flying;

import com.ljw.flying.downloader.HC5AsyncDownImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.Charset;

@SpringBootTest
class FlyingApplicationTests {


    @Test
    public void testHC5() {
        HC5AsyncDownImpl async = new HC5AsyncDownImpl(10000, Charset.defaultCharset());
        String html = async.down("http://idea.94goo.com/key");
        System.out.println(html);
    }

}

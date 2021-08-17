package com.ljw.crawler.utils.detect;

import com.ljw.crawler.utils.detect.impl.EncodingDetectorImpl;

/**
 * @Description: 检测器
 * @Author: langjianwei
 * @DateTime: 2021/6/3 17:14
 * @Version: 1.0.0
 */
public class Detector {

    /**
     * 得到一个EncodingDetector
     * @return
     */
    public static EncodingDetector getEncodingDetector() {
        return new EncodingDetectorImpl();
    }


}

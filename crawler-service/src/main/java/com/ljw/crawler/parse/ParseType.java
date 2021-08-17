package com.ljw.crawler.parse;

import java.lang.annotation.*;

/**
 * @Description: 解析器类型
 * @Author: langjianwei
 * @DateTime: 2021/6/9 15:44
 * @Version: 1.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParseType {

    String name();
}

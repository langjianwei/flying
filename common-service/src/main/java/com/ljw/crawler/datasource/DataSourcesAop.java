package com.ljw.crawler.datasource;

import java.lang.annotation.*;


/**
 * @Author: 郎建伟
 * @Description: 自定义注解，用于aop动态切换数据源
 * @Date: Created in 2020/9/9 14:01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD,
        ElementType.TYPE
})
@Documented
//@Inherited
// @Inherited是一个标记注解,阐述了某个被标注的类型是被继承的。
// 如果一个使用了@Inherited修饰的annotation类型被用于一个class，则这个annotation将被用于该class的子类。
public @interface DataSourcesAop {

    String value() default "db_default";
}

package com.ljw.crawler.datasource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Aspect
@Order(1)
@Component
public class DataSourceAspect {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(DataSourceAop) || @within(DataSourceAop)")
    public void dsPointCut()
    {

    }

    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable
    {
        com.ljw.crawler.datasource.DataSourceAop dataSourceAop = getDataSource(point);

        if (null != dataSourceAop) {
            com.ljw.crawler.datasource.DataSourceContextHolder.setDataSourceType(dataSourceAop.value());
        }
        try {
            return point.proceed();
        } finally {
            // 销毁数据源 在执行方法之后
            com.ljw.crawler.datasource.DataSourceContextHolder.cleanDataSourceType();
        }
    }

    /**
     * 获取需要切换的数据源
     */
    public com.ljw.crawler.datasource.DataSourceAop getDataSource(ProceedingJoinPoint point)
    {
        MethodSignature signature = (MethodSignature) point.getSignature();
        com.ljw.crawler.datasource.DataSourceAop dataSourceAop = AnnotationUtils.findAnnotation(signature.getMethod(), com.ljw.crawler.datasource.DataSourceAop.class);
        if (Objects.nonNull(dataSourceAop)) {
            return dataSourceAop;
        }
        return AnnotationUtils.findAnnotation(signature.getDeclaringType(), com.ljw.crawler.datasource.DataSourceAop.class);
    }

}

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
public class DataSourcesAspect {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(com.ljw.crawler.datasource.DataSourcesAop) || @within(com.ljw.crawler.datasource.DataSourcesAop)")
    public void dsPointCut()
    {

    }

    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable
    {
        DataSourcesAop dataSourcesAop = getDataSource(point);

        if (null != dataSourcesAop) {
            DataSourcesContextHolder.setDataSourceType(dataSourcesAop.value());
        }
        try {
            return point.proceed();
        } finally {
            // 销毁数据源 在执行方法之后
            DataSourcesContextHolder.cleanDataSourceType();
        }
    }

    /**
     * 获取需要切换的数据源
     */
    public DataSourcesAop getDataSource(ProceedingJoinPoint point)
    {
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataSourcesAop dataSourcesAop = AnnotationUtils.findAnnotation(signature.getMethod(), DataSourcesAop.class);
        if (Objects.nonNull(dataSourcesAop)) {
            return dataSourcesAop;
        }
        return AnnotationUtils.findAnnotation(signature.getDeclaringType(), DataSourcesAop.class);
    }

}

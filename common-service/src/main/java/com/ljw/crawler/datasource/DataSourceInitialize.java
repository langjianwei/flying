package com.ljw.crawler.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 郎建伟
 * @Description: 初始化数据源
 * @Date: Created in 2020/9/9 14:18
 */
@Configuration
public class DataSourceInitialize {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DataSourceInfo dataSourceInfo;

    /**
     * @Author: 郎建伟
     * @Description: springboot启动后将自定义创建好的数据源对象,
     *               放到TargetDataSources中用于后续的切换数据源用,
     *               同时指定默认数据源连接
     * @Date: Created in 2019/5/8 18:07
     * @param: []
     * @return: com.eucita.alert.common.dataSource.DynamicDataSource(动态数据源对象)
     */
    @Bean
    public DynamicDataSource dynamicDataSource() {

        // 存储targetDataSources,用于切换数据源时指定对应key即可切换
        Map<Object, Object> targetDataSource = new HashMap<>(8);
        // 实例化所有的DataSource
        logger.info("准备初始化数据源...");
        List<CustomDataSource> dataSourceInfo = this.dataSourceInfo.getList();
        for (CustomDataSource customDataSource : dataSourceInfo) {
            if (customDataSource.getEnable()) {
                HikariDataSource dataSource = createDataSource(customDataSource);
                String dataSourceName = customDataSource.getDataSourceName();
                logger.info("创建数据源：{}", dataSourceName);
                targetDataSource.put(dataSourceName, dataSource);
            }
        }
        DynamicDataSource dynamicDataSource = DynamicDataSource.getInstance();
        dynamicDataSource.setTargetDataSources(targetDataSource);
        // 设置默认数据源
        dynamicDataSource.setDefaultTargetDataSource(targetDataSource.get(this.dataSourceInfo.getDefaultDb()));
        return dynamicDataSource;
    }

    /**
     * @Author: 郎建伟
     * @Description: 创建DataSource
     * @Date: Created in 2020/9/9 14:42
     * @param: [customDataSource]
     * @return: com.zaxxer.hikari.HikariDataSource
     */
    public HikariDataSource createDataSource(CustomDataSource customDataSource){
        // 实例化DataSource
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(customDataSource.getDriverClassName());
        dataSource.setJdbcUrl(customDataSource.getUrl());
        dataSource.setUsername(customDataSource.getUsername());
        dataSource.setPassword(customDataSource.getPassword());
        return dataSource;
    }


    /**
     * 根据数据源创建SqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DynamicDataSource ds) throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        // 指定数据源(这个必须有，否则报错)
        fb.setDataSource(ds);
        // 下边两句仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不加
        fb.setTypeAliasesPackage("com.eucita.crawler.mapper");
        // 指定基包
        fb.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
        // 开启驼峰命名
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        fb.setConfiguration(configuration);

        return fb.getObject();
    }

    /**
     * 配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DynamicDataSource dynamicDataSource) {
        return new DataSourceTransactionManager(dynamicDataSource);
    }

}

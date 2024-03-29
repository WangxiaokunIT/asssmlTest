package com.xinshang.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.xinshang.core.config.properties.DruidProperties;
import com.xinshang.core.config.properties.MutiDataSourceProperties;
import com.xinshang.core.datascope.DataScopeInterceptor;
import com.xinshang.core.mutidatasource.DynamicDataSource;
import com.xinshang.core.mutidatasource.aop.MultiSourceExAop;
import com.xinshang.core.config.properties.DruidProperties;
import com.xinshang.core.config.properties.MutiDataSourceProperties;
import com.xinshang.core.datascope.DataScopeInterceptor;
import com.xinshang.core.mutidatasource.DynamicDataSource;
import com.xinshang.core.mutidatasource.aop.MultiSourceExAop;
import com.xinshang.core.config.properties.DruidProperties;
import com.xinshang.core.config.properties.MutiDataSourceProperties;
import com.xinshang.core.datascope.DataScopeInterceptor;
import com.xinshang.core.mutidatasource.DynamicDataSource;
import com.xinshang.core.mutidatasource.aop.MultiSourceExAop;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * 多数据源配置<br/>
 * <p>
 * 注：由于引入多数据源，所以让spring事务的aop要在多数据源切换aop的后面
 *
 * @author zhangjiajia
 * @date 2017/5/20 21:58
 */
@Configuration
@ConditionalOnProperty(prefix = "asmall.muti-datasource", name = "open", havingValue = "true")
@EnableTransactionManagement(order = 2)
@MapperScan(basePackages = {"com.xinshang.modular.*.dao","com.xinshang.multi.mapper"})
public class MultiDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "asmall.muti-datasource")
    public MutiDataSourceProperties mutiDataSourceProperties() {
        return new MutiDataSourceProperties();
    }

    @Bean
    public MultiSourceExAop multiSourceExAop() {
        return new MultiSourceExAop();
    }

    /**
     * asmall的数据源
     */
    private DruidDataSource dataSource(DruidProperties druidProperties) {
        DruidDataSource dataSource = new DruidDataSource();
        druidProperties.config(dataSource);
        return dataSource;
    }

    /**
     * 多数据源，第二个数据源
     */
    private DruidDataSource bizDataSource(DruidProperties druidProperties, MutiDataSourceProperties mutiDataSourceProperties) {
        DruidDataSource dataSource = new DruidDataSource();
        druidProperties.config(dataSource);
        mutiDataSourceProperties.config(dataSource);
        return dataSource;
    }

    /**
     * 多数据源连接池配置
     */
    @Bean
    public DynamicDataSource mutiDataSource(DruidProperties druidProperties, MutiDataSourceProperties mutiDataSourceProperties) {

        DruidDataSource dataSourceasmall = dataSource(druidProperties);
        DruidDataSource bizDataSource = bizDataSource(druidProperties, mutiDataSourceProperties);

        try {
            dataSourceasmall.init();
            bizDataSource.init();
        } catch (SQLException sql) {
            sql.printStackTrace();
        }

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put(mutiDataSourceProperties.getDataSourceNames()[0], dataSourceasmall);
        hashMap.put(mutiDataSourceProperties.getDataSourceNames()[1], bizDataSource);
        dynamicDataSource.setTargetDataSources(hashMap);
        dynamicDataSource.setDefaultTargetDataSource(dataSourceasmall);
        return dynamicDataSource;
    }

    /**
     * mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 开启 PageHelper 的支持
        paginationInterceptor.setLocalPage(true);
        return paginationInterceptor;
    }

    /**
     * 数据范围mybatis插件
     */
    @Bean
    public DataScopeInterceptor dataScopeInterceptor() {
        return new DataScopeInterceptor();
    }

    /**
     * 乐观锁mybatis插件
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    /**
     * 事务配置
     *
     * @author zhangjiajia
     * @date 2018/6/27 23:11
     */
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(DynamicDataSource mutiDataSource) {
        return new DataSourceTransactionManager(mutiDataSource);
    }
}

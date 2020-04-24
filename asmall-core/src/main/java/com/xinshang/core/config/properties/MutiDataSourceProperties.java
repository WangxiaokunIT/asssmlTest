package com.xinshang.core.config.properties;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;

/**
 * 默认多数据源配置
 *
 * @author fengshuonan
 * @date 2017-08-16 10:02
 */
@Data
public class MutiDataSourceProperties {

    private String url = "jdbc:mysql://127.0.0.1:3306/biz?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull";

    private String username = "root";

    private String password = "root";

    private String driverClassName = "com.mysql.cj.jdbc.Driver";

    private String validationQuery = "SELECT 'x'";

    private String[] dataSourceNames = {"dataSourceasmall", "dataSourceBiz"};

    public void config(DruidDataSource dataSource) {
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setValidationQuery(validationQuery);
    }

}

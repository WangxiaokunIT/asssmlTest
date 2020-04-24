package com.xinshang.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 读取通联预设字典项
 * @author lyk
 */
@Data
@Component
@PropertySource(value = "classpath:config/application-code.yml",encoding = "utf-8")
@ConfigurationProperties(prefix = AllinPayCodeProperties.ALLIN_PAY_CODE_PREFIX)
public class AllinPayCodeProperties {
    public static final String ALLIN_PAY_CODE_PREFIX = "allin-pay-code";

    /**
     * 6.11业务码
     */
    @Value("${business-code}")
    private String businessCode;


    /**
     * 代收投资金
     */
    @Value("${collection-code}")
    private String collectionCode;

    /**
     * 行业其他码
     */
    @Value("${other-code}")
    private String otherCode;

    /**
     * 行业其他名
     */
    @Value("${other-name}")
    private String otherName;

    /**
     * 提现方式
     */
    @Value("${withdraw-type}")
    private String withdrawType;


}

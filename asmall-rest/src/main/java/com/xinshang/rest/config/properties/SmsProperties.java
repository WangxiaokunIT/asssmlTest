package com.xinshang.rest.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * aliyunOSS存储
 *
 * @author zhangjaijia
 * @Date 2018年9月11日 16:15:46
 */

@Data
@Component
@ConfigurationProperties(prefix = SmsProperties.ALIYUNSMSCONF_PREFIX)
public class SmsProperties {

    public static final String ALIYUNSMSCONF_PREFIX = "aliyun.sms";

    /**
     *  地区
     */
    private String regionId;
    /**
     * 访问ID
     */
    private String accessKeyId;
    /**
     * 访问密钥
     */
    private String accessKeySecret;
    /**
     * 域名
     */
    private String domain;
    /**
     * 模板编码
     */
    private String templateCode;
    /**
     * 每日发短信上线
     */
    private Integer dayLimit;

}

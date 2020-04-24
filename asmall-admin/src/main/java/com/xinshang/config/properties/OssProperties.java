package com.xinshang.config.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * aliyunOSS存储
 *
 * @author zhangjaijia
 * @date 2018年9月11日 16:15:46
 */

@Data
@Component
@ConfigurationProperties(prefix = OssProperties.ALIYUNOSSCONF_PREFIX)
public class OssProperties {

    public static final String ALIYUNOSSCONF_PREFIX = "aliyun.oss";

    /**
     * 访问域名
     */
    private String endpoint;

    /**
     *  访问ID
     */
    private String accessKeyId;

    /**
     *    访问密钥
     */
    private String accessKeySecret;

    /**
     *   存储空间
     */
    private String bucketName;

    /**
     *     存储目录
     */
    private String dir;

}

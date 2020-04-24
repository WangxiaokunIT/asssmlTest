package com.xinshang.config.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * asmall项目配置
 *
 * @author zhangjiajia
 * @date 2017/5/23 22:31
 */

@Data
@Component
@ConfigurationProperties(prefix = SystemProperties.PREFIX)
public class SystemProperties {

    /**
     * 数据库名称
     */
    public static final String PREFIX = "asmall";

    /**
     * 验证码是否开启
     */
    private Boolean kaptchaOpen = false;

    /**
     * 接口文档是否开启
     */
    private Boolean swaggerOpen = false;

    /**
     * 默认文件上传目录
     */
    private String fileUploadPath;

    /**
     * 目录是否已存在
     */
    private Boolean haveCreatePath = false;

    /**
     * 是否开启session共享
     */
    private Boolean springSessionOpen = false;

    /**
     * 阿里云OSS是否开启
     */
    private Boolean ossOpen = true;

    /**
     * session 失效时间（默认为30分钟 单位：秒）
     */
    private Integer sessionInvalidateTime = 30 * 60;

    /**
     * session 验证失效时间（默认为15分钟 单位：秒）
     */
    private Integer sessionValidationInterval = 15 * 60;

    /**特殊字符过滤路径**/
    private List<String> xssFilterUrls;

    /**
     * 项目地址
     */
    private String projectUrl;

    /**
     * 系统到期时间
     */
    private String limitDate;


}

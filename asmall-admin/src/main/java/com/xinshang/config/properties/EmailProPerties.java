package com.xinshang.config.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 邮件发送
 * @date:      2019-1-18 14:48
 * @author     lyk
 * @version    V1.0
 * @author zhangjiajia
 */

@Data
@Component
@ConfigurationProperties(prefix = EmailProPerties.ALIYUNOSSCONF_PREFIX)
public class EmailProPerties {

    public static final String ALIYUNOSSCONF_PREFIX = "system.email";

    /**
     * 发送人昵称
     */
    private String nickname;
    /**
     * 邮箱账号
     */
    private String username;
    /**
     * 邮箱密码（个人：需设置）
     */
    private String password;
    /**
     * 协议
     */
    private String protocol;
    /**
     * 服务器地址（邮件服务器）
     */
    private String host;
    /**
     * 端口
     */
    private String port;
    /**
     * 是否为企业邮箱  true企业   false 个人
     */
    private Boolean enterprise;

}

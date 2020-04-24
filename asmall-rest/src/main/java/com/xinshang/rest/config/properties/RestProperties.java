package com.xinshang.rest.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * upms项目配置
 *
 * @author stylefeng
 * @Date 2017/5/23 22:31
 */
@Data
@Component
@ConfigurationProperties(prefix = RestProperties.PREFIX)
public class RestProperties {

    public static final String PREFIX = "rest";

    /**
     * 开启接口文档
     */
    private Boolean swaggerOpen = false;

    /**
     * 校验签名
     */
    private Boolean signOpen = true;

    /**
     * jwt验证
     */
    private Boolean authOpen = true;

    /**
     * 项目地址
     */
    private String projectUrl;

    /**
     * 微信公众号appid
     */

    private String weChatPublicAddressAppId;

    /**
     * 微信公众号appSecret
     */
    private String  weChatPublicAddressAppSecret;

    /**
     *  获取openid生成token后回调此地址
     */
    private String weChatFrontCallbackUrl;

}

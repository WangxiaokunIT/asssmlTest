package com.xinshang.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangjiajia
 */
@Data
@Configuration
@ConfigurationProperties(prefix = AllinPayProperties.ALLIN_PAY_PREFIX)
public class AllinPayProperties {

    public static final String ALLIN_PAY_PREFIX = "allinpay";

    /**
     * 应用系统编号
     */
    private String sysId;

    /**
     * 接口地址
     */
    private String serverUrl;

    /**
     * 私钥/公钥证书路径
     */
    private String path;

    /**
     * 证书密码
     */
    private String pwd;

    /**
     * 别名
     */
    private String alias;

    /**
     * 接口版本
     */
    private String version;

    /**
     * 生产环境请使用生产证书
     */
    private String tlCertPath;

    /**
     * 通联电子协议签约地址
     */
    private String signContractUrl;

    /**
     * 设置支付密码地址
     */
    private String setPayPwdUrl;

    /**
     * 修改支付密码地址
     */
    private String updatePayPwdUrl;

    /**
     * 重置支付密码地址
     */
    private String resetPayPwdUrl;

    /**
     * 托管账户集编号
     */
    private String accountSetNo;

    /**
     * 确认支付地址
     */
    private String payOrderUrl;
    /**
     * 微信端应用 ID
     */
    private String subAppId;

    /**
     * 分账人
     */
    private String bizUserId;

    /**
     * 密码确认支付地址
     */
    private String payConfirmPasswordUrl;
}

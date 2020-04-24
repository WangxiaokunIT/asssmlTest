package com.xinshang.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * eqb项目配置
 *
 * @author stylefeng
 * @Date 2017/5/23 22:31
 */
@Data
@Component
@ConfigurationProperties(prefix = EqbProperties.PREFIX)
public class EqbProperties {

    public static final String PREFIX = "eqb";

    /**
     * 公司印章
     */
    private String aisaiSeal;

    /**
     * 法人印章
      */
    private String legalSeal;

    /**
     * 模板地址
     */
    private String templatePath;

    /**
     * 字体路径
     */
    private String fontPath;

    /**
     * 项目id
     */
    private String projectId ;

    private String projectSecret ;

    private String kjeInitUrl ;

    private String httpBusUrl ;

    private String httpSceneUrl ;

    private String httpSegUrl ;

    private String httpSegpropUrl ;

    private String httpOriginalStandardUrl ;

    private String httpOriginalAdvancedUrl ;

    private String httpOriginalDigestUrl ;

    private String httpVoucherUrl ;

    private String httpVoucherAppendUrl ;

    private String httpRelateUrl ;

    private String httpsViewpageUrl ;

}

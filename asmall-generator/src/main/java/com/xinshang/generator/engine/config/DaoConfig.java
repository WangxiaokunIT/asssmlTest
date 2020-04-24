package com.xinshang.generator.engine.config;

import lombok.*;

/**
 * Dao模板生成的配置
 *
 * @author fengshuonan
 * @date 2017-05-07 22:12
 */

@Data
public class DaoConfig {

    private ContextConfig contextConfig;
    private String daoPathTemplate;
    private String xmlPathTemplate;
    private String packageName;

    public void init() {
        this.daoPathTemplate = "\\src\\main\\java\\" + contextConfig.getProPackage().replaceAll("\\.", "\\\\") + "\\modular\\" + contextConfig.getModuleName() + "\\dao\\{}Dao.java";
        this.xmlPathTemplate = "\\src\\main\\resources\\mapper\\"+ contextConfig.getModuleName()+"\\{}Dao.xml";
        this.packageName = contextConfig.getProPackage() + ".modular." + contextConfig.getModuleName() + ".dao";
    }

}

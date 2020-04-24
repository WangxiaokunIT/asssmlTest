package com.xinshang.generator.engine.config;


import lombok.*;

import java.io.File;

/**
 * 全局配置
 *
 * @author fengshuonan
 * @date 2017-05-08 20:21
 */

@Data
public class ContextConfig {

    private String templatePrefixPath = "templates"+ File.separator+"advanced";
    /**模板输出的项目目录**/
    private String projectPath = "D:\\ideaSpace\\asmall";
    /**业务名称**/
    private String bizChName;
    /**业务英文名称**/
    private String bizEnName;
    /**业务英文名称(大写)**/
    private String bizEnBigName;
    /**模块名称**/
    private String moduleName = "system";
    /**作者**/
    private String author;

    private String proPackage = "com.xinshang";
    private String coreBasePackage = "com.xinshang.core";
    /**model的包名**/
    private String modelPackageName = "com.xinshang.modular.system.model";
    /**model的dao**/
    private String modelMapperPackageName = "com.xinshang.modular.system.dao";
    /**实体的名称**/
    private String entityName;

    /**是否生成控制器代码开关**/
    private Boolean controllerSwitch = true;
    /**主页**/
    private Boolean indexPageSwitch = true;
    /**添加页面**/
    private Boolean addPageSwitch = true;
    /**编辑页面**/
    private Boolean editPageSwitch = true;
    /**js**/
    private Boolean jsSwitch = true;
    /**详情页面js**/
    private Boolean infoJsSwitch = true;
    /**dao**/
    private Boolean daoSwitch = true;
    /**service**/
    private Boolean serviceSwitch = true;
    /**生成实体的开关**/
    private Boolean entitySwitch = true;
    /**生成sql的开关**/
    private Boolean sqlSwitch = true;


    public void init() {
        if (entityName == null) {
            entityName = bizEnBigName;
        }
        modelPackageName = proPackage + "." + "modular.system.model";
        modelMapperPackageName = proPackage + "." + "modular.system.dao";
    }

}

package com.xinshang.generator.engine.config;

import lombok.*;

import java.io.File;

/**
 * 页面 模板生成的配置
 *
 * @author fengshuonan
 * @date 2017-05-07 22:12
 */

@Data
public class PageConfig {

    private ContextConfig contextConfig;

    private String pagePathTemplate;
    private String pageAddPathTemplate;
    private String pageEditPathTemplate;
    private String pageJsPathTemplate;
    private String pageInfoJsPathTemplate;
    private String xmlMapperPathTemplate;

    public void init() {
        pagePathTemplate = "\\src\\main\\resources\\templates"+ File.separator + contextConfig.getModuleName() + "\\{}\\{}.html";
        pageAddPathTemplate = "\\src\\main\\resources\\templates"+ File.separator + contextConfig.getModuleName() + "\\{}\\{}_add.html";
        pageEditPathTemplate = "\\src\\main\\resources\\templates"+ File.separator +contextConfig.getModuleName() + "\\{}\\{}_edit.html";
        pageJsPathTemplate = "\\src\\main\\resources\\static\\modular\\" + contextConfig.getModuleName() + "\\{}\\{}.js";
        pageInfoJsPathTemplate = "\\src\\main\\resources\\static\\modular\\" + contextConfig.getModuleName() + "\\{}\\{}_info.js";
        xmlMapperPathTemplate = "\\src\\main\\resources\\mppper\\" + contextConfig.getModuleName() + "\\{}\\{}_info.js";
    }

}

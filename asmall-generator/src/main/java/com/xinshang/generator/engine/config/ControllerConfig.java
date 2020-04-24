package com.xinshang.generator.engine.config;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 控制器模板生成的配置
 *
 * @author fengshuonan
 * @date 2017-05-07 22:12
 */

@Data
public class ControllerConfig {

    private ContextConfig contextConfig;

    private String controllerPathTemplate;
    /**
     *包名称
     */
    private String packageName;
    /**
     *  所引入的包
     */
    private List<String> imports;

    public void init() {
        ArrayList<String> imports = new ArrayList<>();
        imports.add(contextConfig.getCoreBasePackage() + ".base.controller.BaseController");
        imports.add("org.springframework.stereotype.Controller");
        imports.add("org.springframework.web.bind.annotation.RequestMapping");
        imports.add("org.springframework.web.bind.annotation.ResponseBody");
        imports.add("org.springframework.ui.Model");
        imports.add("com.xinshang.core.support.BeanKit");
        imports.add("com.baomidou.mybatisplus.mapper.EntityWrapper");
        imports.add("com.baomidou.mybatisplus.plugins.Page");
        imports.add("com.xinshang.core.common.constant.factory.PageFactory");
        imports.add("java.util.Map");
        imports.add("java.util.Arrays");
        imports.add("cn.hutool.core.util.StrUtil");
        imports.add("org.springframework.web.bind.annotation.PathVariable");
        imports.add("org.springframework.beans.factory.annotation.Autowired");
        imports.add(contextConfig.getProPackage() + ".core.log.LogObjectHolder");
        imports.add("org.springframework.web.bind.annotation.RequestParam");
        imports.add(contextConfig.getProPackage() + ".modular." + contextConfig.getModuleName() + ".model." + contextConfig.getEntityName());
        imports.add(contextConfig.getProPackage() + ".modular." + contextConfig.getModuleName() + ".service" + ".I" + contextConfig.getEntityName() + "Service");
        this.imports = imports;
        this.packageName = contextConfig.getProPackage() + ".modular." + contextConfig.getModuleName() + ".controller";
        this.controllerPathTemplate = "\\src\\main\\java\\"+contextConfig.getProPackage().replaceAll("\\.","\\\\")+"\\modular\\" + contextConfig.getModuleName() + "\\controller\\{}Controller.java";
    }

}

package com.xinshang.generator.engine.config;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Service模板生成的配置
 *
 * @author fengshuonan
 * @date 2017-05-07 22:12
 */

@Data
public class ServiceConfig {

    private ContextConfig contextConfig;

    private String servicePathTemplate;
    private String serviceImplPathTemplate;

    private String packageName;

    private List<String> serviceInterfaceImports;
    private List<String> serviceImplImports;

    public void init() {
        ArrayList<String> imports = new ArrayList<>();
        imports.add("org.springframework.stereotype.Service");
        imports.add("com.baomidou.mybatisplus.service.impl.ServiceImpl");
        imports.add(contextConfig.getModelPackageName() + "." + contextConfig.getEntityName());
        imports.add(contextConfig.getModelMapperPackageName() + "." + contextConfig.getEntityName() + "Mapper");
        imports.add(contextConfig.getProPackage() + ".modular." + contextConfig.getModuleName() + ".service.I" + contextConfig.getBizEnBigName() + "Service");
        this.serviceImplImports = imports;

        ArrayList<String> interfaceImports = new ArrayList<>();
        interfaceImports.add("com.baomidou.mybatisplus.service.IService");
        interfaceImports.add(contextConfig.getModelPackageName() + "." + contextConfig.getEntityName());
        this.serviceInterfaceImports = interfaceImports;

        this.servicePathTemplate = "\\src\\main\\java\\" + contextConfig.getProPackage().replaceAll("\\.", "\\\\") + "\\modular\\" + contextConfig.getModuleName() + "\\service\\I{}Service.java";
        this.serviceImplPathTemplate = "\\src\\main\\java\\" + contextConfig.getProPackage().replaceAll("\\.", "\\\\") + "\\modular\\" + contextConfig.getModuleName() + "\\service\\impl\\{}ServiceImpl.java";
        this.packageName = contextConfig.getProPackage() + ".modular." + contextConfig.getModuleName() + ".service";
    }


}

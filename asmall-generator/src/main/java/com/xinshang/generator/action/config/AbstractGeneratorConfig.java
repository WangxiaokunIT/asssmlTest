package com.xinshang.generator.action.config;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.xinshang.core.util.FileUtil;
import com.xinshang.generator.engine.SimpleTemplateEngine;
import com.xinshang.generator.engine.base.SystemTemplateEngine;
import com.xinshang.generator.engine.config.ContextConfig;
import com.xinshang.generator.engine.config.SqlConfig;
import java.io.File;
import java.util.List;

/**
 * 代码生成的抽象配置
 *
 * @author fengshuonan
 * @date 2017-10-28-下午8:22
 */
public abstract class AbstractGeneratorConfig {

    /**
     * mybatis-plus代码生成器配置
     */
    GlobalConfig globalConfig = new GlobalConfig();

    DataSourceConfig dataSourceConfig = new DataSourceConfig();

    StrategyConfig strategyConfig = new StrategyConfig();

    PackageConfig packageConfig = new PackageConfig();

    TableInfo tableInfo = null;

    /**
     * asmall代码生成器配置
     */
    ContextConfig contextConfig = new ContextConfig();

    SqlConfig sqlConfig = new SqlConfig();

    protected abstract void config();

    public void init() {
        config();
        packageConfig.setParent(null);
        packageConfig.setService(contextConfig.getProPackage() + ".modular." + contextConfig.getModuleName() + ".service");
        packageConfig.setServiceImpl(contextConfig.getProPackage() + ".modular." + contextConfig.getModuleName() + ".service.impl");
        packageConfig.setEntity(contextConfig.getProPackage()+ ".modular."+contextConfig.getModuleName()+".model");
        packageConfig.setMapper(contextConfig.getProPackage() + ".modular."+contextConfig.getModuleName()+".dao");
        packageConfig.setXml(contextConfig.getProPackage() + ".modular."+contextConfig.getModuleName()+".dao.mapping");

        //controller没用掉,生成之后会自动删掉
        packageConfig.setController("TTT");

        if (!contextConfig.getEntitySwitch()) {
            packageConfig.setEntity("TTT");
        }

        if (!contextConfig.getDaoSwitch()) {
            packageConfig.setMapper("TTT");
            packageConfig.setXml("TTT");
        }

        if (!contextConfig.getServiceSwitch()) {
            packageConfig.setService("TTT");
            packageConfig.setServiceImpl("TTT");
        }

    }

    /**
     * 删除不必要的代码
     */
    public void destory() {
        String outputDir = globalConfig.getOutputDir() + "/TTT";
        FileUtil.deleteDir(new File(outputDir));
    }

    public AbstractGeneratorConfig() {
    }

    public void doMpGeneration() {
        init();
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setGlobalConfig(globalConfig);
        autoGenerator.setDataSource(dataSourceConfig);
        autoGenerator.setStrategy(strategyConfig);
        autoGenerator.setPackageInfo(packageConfig);
        autoGenerator.execute();
        destory();

        //获取table信息,用于asmall代码生成
        List<TableInfo> tableInfoList = autoGenerator.getConfig().getTableInfoList();
        if (tableInfoList != null && tableInfoList.size() > 0) {
            this.tableInfo = tableInfoList.get(0);
        }
    }

    public void doasmallGeneration() {
        SystemTemplateEngine asmallTemplateEngine = new SimpleTemplateEngine();
        asmallTemplateEngine.setContextConfig(contextConfig);
        sqlConfig.setConnection(dataSourceConfig.getConn());
        asmallTemplateEngine.setSqlConfig(sqlConfig);
        asmallTemplateEngine.setTableInfo(tableInfo);
        asmallTemplateEngine.start();
    }
}

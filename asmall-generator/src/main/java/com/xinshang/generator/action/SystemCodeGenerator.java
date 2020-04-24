package com.xinshang.generator.action;


import com.xinshang.generator.action.config.SystemGeneratorConfig;

/**
 * 代码生成器,可以生成实体,dao,service,controller,html,js
 *
 * @author zhangjiajia
 * @date 2017/5/21 12:38
 */
public class SystemCodeGenerator {

    public static void main(String[] args) {

        /**
         * Mybatis-Plus的代码生成器:
         *      mp的代码生成器可以生成实体,mapper,mapper对应的xml,service
         */
        SystemGeneratorConfig asmallGeneratorConfig = new SystemGeneratorConfig();
        asmallGeneratorConfig.doMpGeneration();

        /**
         * asmall的生成器:
         *      asmall的代码生成器可以生成controller,html页面,页面对应的js
         */
        asmallGeneratorConfig.doasmallGeneration();
    }

}
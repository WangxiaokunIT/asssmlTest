package com.xinshang.generator.action.model;

import lombok.Data;

/**
 * 代码生成的查询参数
 *
 * @author fengshuonan
 * @date 2017-11-30-下午2:05
 */
@Data
public class GenQo {

    /**
     * 数据库账号
     */
    private String userName;

    /**
     * 数据库密码
     */
    private String password;

    /**
     * 数据库url
     */
    private String url;

    /**
     * 项目地址
     */
    private String projectPath;

    /**
     * 作者
     */
    private String author;

    /**
     * 项目的包
     */
    private String projectPackage;

    /**
     * 核心模块的包
     */
    private String corePackage;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 忽略的表前缀
     */
    private String ignoreTabelPrefix;

    /**
     * 业务名称
     */
    private String bizName;

    /**
     * 模块名
     */
    private String moduleName;
    
    /**
     * 父级菜单名称
     */
    private String parentMenuName;

    /**
     * 是否生成控制器代码开关
     */
    private Boolean controllerSwitch = false;

    /**
     * 主页
     */
    private Boolean indexPageSwitch = false;

    /**
     * 添加页面
     */
    private Boolean addPageSwitch = false;

    /**
     * 编辑页面
     */
    private Boolean editPageSwitch = false;

    /**
     * 主页的js
     */
    private Boolean jsSwitch = false;

    /**
     * 详情页面js
     */
    private Boolean infoJsSwitch = false;

    /**
     * dao的开关
     */
    private Boolean daoSwitch = false;

    /**
     * service
     */
    private Boolean serviceSwitch = false;

    /**
     * 生成实体的开关
     */
    private Boolean entitySwitch = false;

    /**
     * 生成sql的开关
     */
    private Boolean sqlSwitch = false;

}

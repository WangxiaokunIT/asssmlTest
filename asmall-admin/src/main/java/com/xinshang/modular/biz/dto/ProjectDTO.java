package com.xinshang.modular.biz.dto;

import lombok.Data;

/**
 * 项目查询对象
 * @author lyk
 */
@Data
public class ProjectDTO {

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目编号
     */
    private String projectNumber;

    /**
     * 供应商姓名
     */
    private String supplierName;

    /**
     * 审核状态
     */
    private Integer state;

    /**
     * 请求类型
     */
    private Integer pageType;

    /**
     * 状态集合
     */
    private String states;


}

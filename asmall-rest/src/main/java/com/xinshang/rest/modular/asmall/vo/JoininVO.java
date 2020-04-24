package com.xinshang.rest.modular.asmall.vo;

import com.xinshang.rest.modular.asmall.model.Joinin;
import com.xinshang.rest.modular.asmall.model.Project;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目增强显示对象
 * @author lyk
 */
@Data
@ApiModel("项目信息")
public class JoininVO extends Joinin {
    /**
     * 项目编号
     */
    @ApiModelProperty(value = "项目编号",example="20191017002",required=true)
    private String number;
    /**
     * 加盟名称
     */
    @ApiModelProperty(value = "加盟名称",example="测试加盟",required=true)
    private String name;

    /**
     * 加盟简介
     */
    @ApiModelProperty(value = "加盟简介",example="测试加盟信息",required=true)
    private String briefIntroduction;

    /**
     * 招募周期
     */
    @ApiModelProperty(value = "招募周期",example="3",required=true)
    private Integer recruitmentCycle;

    /**
     * 招募周期单位
     */
    @ApiModelProperty(value = "周期单位",example="1",required=true)
    private String unit;

    /**
     * 权益率
     */
    @ApiModelProperty(value = "权益率",example="1.1",required=true)
    private BigDecimal equityRate;
    /**
     * 加盟金额
     */
    @ApiModelProperty(value = "招募金额",example="1000.00",required=true)
    private BigDecimal investmentAmount;
    /**
     * 加盟状态
     */
    @ApiModelProperty(value = "招募状态",example="1.申请中 2. 招募中3未通过 4招募结束 5招募完成 6提现申请中 7招募待还 8招募已还",required=true)
    private Integer state;

    /**
     * 归还方式
     */
    @ApiModelProperty(value = "归还方式",example="0",required=true)
    private Integer repaymentMethod;

    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID",example="1",required=true)
    private Long projectId;

    /**
     * 加盟权益详情
     */
    @ApiModelProperty(value = "加盟权益详情",example="XXX",required=true)
    private String details;
    /**
     * 还款开始时间
     */
    @ApiModelProperty(value = "还款开始时间",example="2019-01-01",required=true)
    private Date startRecordTime;
    /**
     *  还款结束时间
     */
    @ApiModelProperty(value = " 还款结束时间",example="2019-01-01",required=true)
    private Date endRecordTime;

    /**
     * 合同ID
     */
    @ApiModelProperty(value = "合同ID",example="1",required=true)
    private Long id;


}

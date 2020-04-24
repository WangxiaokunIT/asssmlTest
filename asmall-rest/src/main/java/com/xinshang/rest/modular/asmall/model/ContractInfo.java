package com.xinshang.rest.modular.asmall.model;

import com.baomidou.mybatisplus.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 发布招募信息申请
 * </p>
 *
 * @author lvyingkai
 * @since 2019-10-16
 */
@Data
public class ContractInfo extends Model<ContractInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 项目编号（如：20191016001）
     */
    private String number;
    /**
     * 项目名称
     */
    private String name;
    /**
     * 项目简介
     */
    private String briefIntroduction;
    /**
     * 项目招募开始时间
     */
    private Date startTime;
    /**
     * 项目招募结束时间
     */
    private Date endTime;
    /**
     * 供应商id
     */
    private Long supplierId;
    /**
     * 最低加盟金额（1000的倍数）
     */
    private BigDecimal minMoney;
    /**
     * 最高加盟金额（1000的倍数）
     */
    private BigDecimal maxMoney;
    /**
     * 归还方式（字典表）
     */
    private Long repaymentMethod;
    /**
     * 招募周期
     */
    private Integer recruitmentCycle;
    /**
     * 单位（天、月）
     */
    private String unit;
    /**
     * 权益率
     */
    private BigDecimal equityRate;
    /**
     * 加盟权益详情
     */
    private String details;

    /**
     * 供应商地址
     */
    private String company_address;

    /**
     * 法人代表
     */
    private String legal_name;

    /**
     * 供应商名称
     */
    private String sname;

    @Override
    public String toString() {
        return "Project{" +
        ", number=" + number +
        ", name=" + name +
        ", briefIntroduction=" + briefIntroduction +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", supplierId=" + supplierId +
        ", minMoney=" + minMoney +
        ", maxMoney=" + maxMoney +
        ", repaymentMethod=" + repaymentMethod +
        ", recruitmentCycle=" + recruitmentCycle +
        ", unit=" + unit +
        ", equityRate=" + equityRate +
        ", details=" + details +
        "}";
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }
}

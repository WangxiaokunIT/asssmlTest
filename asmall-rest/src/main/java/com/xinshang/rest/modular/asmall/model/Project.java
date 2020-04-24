package com.xinshang.rest.modular.asmall.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 发布招募信息申请
 * </p>
 *
 * @author lvyingkai
 * @since 2019-10-16
 */
@TableName("tb_project")
@Data
public class Project extends Model<Project> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
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
    @TableField("brief_introduction")
    private String briefIntroduction;
    /**
     * 项目招募开始时间
     */
    @TableField("start_time")
    private Date startTime;
    /**
     * 项目招募结束时间
     */
    @TableField("end_time")
    private Date endTime;
    /**
     * 供应商id
     */
    @TableField("supplier_id")
    private Long supplierId;
    /**
     * 最低加盟金额（1000的倍数）
     */
    @TableField("min_money")
    private BigDecimal minMoney;
    /**
     * 最高加盟金额（1000的倍数）
     */
    @TableField("max_money")
    private BigDecimal maxMoney;
    /**
     * 归还方式（字典表）
     */
    @TableField("repayment_method")
    private Long repaymentMethod;
    /**
     * 招募周期
     */
    @TableField("recruitment_cycle")
    private Integer recruitmentCycle;
    /**
     * 单位（天、月）
     */
    private String unit;
    /**
     * 权益率
     */
    @TableField("equity_rate")
    private BigDecimal equityRate;
    /**
     * 加盟权益详情
     */
    private String details;

    /**
     * 开始记录时间
     */
    @TableField("start_record_time")
    private Date startRecordTime;

    /**
     * 开始记录时间
     */
    @TableField("end_record_time")
    private Date endRecordTime;

    /**
     * 状态
     */
    private Integer state;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建人id
     */
    @TableField("create_user_id")
    private Long createUserId;




    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Project{" +
        "id=" + id +
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
        ", state=" + state +
        ", createTime=" + createTime +
        ", createUserId=" + createUserId +
        "}";
    }
}

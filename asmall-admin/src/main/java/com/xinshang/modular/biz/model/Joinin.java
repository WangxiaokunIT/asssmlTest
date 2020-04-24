package com.xinshang.modular.biz.model;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 加盟表
 * </p>
 *
 * @author zhoushuai
 * @since 2019-10-25
 */
@TableName("tb_joinin")
public class Joinin extends Model<Joinin> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId("join_id")
    private Long joinId;
    /**
     * 客户id
     */
    @TableField("custom_id")
    private Long customId;
    /**
     * 招募信息id
     */
    @TableField("project_id")
    private Long projectId;
    /**
     * 投资金额
     */
    @TableField("investment_amount")
    private BigDecimal investmentAmount;
    /**
     * 投资时间
     */
    @TableField("investment_time")
    private Date investmentTime;
    /**
     * 支付方式
     */
    @TableField("payment_method")
    private Integer paymentMethod;
    /**
     * 状态
     */
    private Integer Status;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建人ID
     */
    @TableField("create_user_id")
    private Integer createUserId;


    public String getBizOrderNo() {
        return bizOrderNo;
    }

    public void setBizOrderNo(String bizOrderNo) {
        this.bizOrderNo = bizOrderNo;
    }

    /**
     * 订单编号
     */
    @TableField("biz_order_no")
    private String bizOrderNo;


    public Long getJoinId() {
        return joinId;
    }

    public void setJoinId(Long joinId) {
        this.joinId = joinId;
    }

    public Long getCustomId() {
        return customId;
    }

    public void setCustomId(Long customId) {
        this.customId = customId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public BigDecimal getInvestmentAmount() {
        return investmentAmount;
    }

    public void setInvestmentAmount(BigDecimal investmentAmount) {
        this.investmentAmount = investmentAmount;
    }

    public Date getInvestmentTime() {
        return investmentTime;
    }

    public void setInvestmentTime(Date investmentTime) {
        this.investmentTime = investmentTime;
    }

    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer Status) {
        this.Status = Status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    @Override
    protected Serializable pkVal() {
        return this.joinId;
    }

    @Override
    public String toString() {
        return "Joinin{" +
        "joinId=" + joinId +
        ", customId=" + customId +
        ", projectId=" + projectId +
        ", investmentAmount=" + investmentAmount +
        ", investmentTime=" + investmentTime +
        ", paymentMethod=" + paymentMethod +
        ", Status=" + Status +
        ", createTime=" + createTime +
        ", createUserId=" + createUserId +
        ", bizOrderNo=" + bizOrderNo +
        "}";
    }
}

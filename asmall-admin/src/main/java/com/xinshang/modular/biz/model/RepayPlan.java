package com.xinshang.modular.biz.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-10-25
 */
@TableName("tb_repay_plan")
public class RepayPlan extends Model<RepayPlan> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 招募ID
     */
    @TableField("project_id")
    private Long projectId;
    /**
     * 加盟ID
     */
    @TableField("joinin_id")
    private Integer joininId;
    /**
     * 客户ID
     */
    @TableField("member_id")
    private Integer memberId;
    /**
     * 投资金额
     */
    @TableField("lend_amount")
    private BigDecimal lendAmount;
    /**
     * 应还本息
     */
    @TableField("paid_mort")
    private BigDecimal paidMort;
    /**
     * 实还本息
     */
    @TableField("have_mort")
    private BigDecimal haveMort;
    /**
     * 应还本金
     */
    @TableField("paid_tim")
    private BigDecimal paidTim;
    /**
     * 实还本金
     */
    @TableField("have_tim")
    private BigDecimal haveTim;
    /**
     * 应还利息
     */
    @TableField("paid_inter")
    private BigDecimal paidInter;
    /**
     * 实还利息
     */
    @TableField("have_inter")
    private BigDecimal haveInter;
    /**
     * 还款期数
     */
    private Integer months;
    /**
     * 还款状态
     */
    private Integer statecode;
    /**
     * 应还本金日期
     */
    @TableField("paid_tim_date")
    private Date paidTimDate;
    /**
     * 实还本金日期
     */
    @TableField("have_tim_date")
    private Date haveTimDate;
    /**
     * 应还利息日期
     */
    @TableField("paid_inter_date")
    private Date paidInterDate;
    /**
     * 实还利息日期
     */
    @TableField("have_inter_date")
    private Date haveInterDate;

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 是否删除
     */
    @TableField("is_delete")
    private Integer isDelete;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Integer getJoininId() {
        return joininId;
    }

    public void setJoininId(Integer joininId) {
        this.joininId = joininId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public BigDecimal getLendAmount() {
        return lendAmount;
    }

    public void setLendAmount(BigDecimal lendAmount) {
        this.lendAmount = lendAmount;
    }

    public BigDecimal getPaidMort() {
        return paidMort;
    }

    public void setPaidMort(BigDecimal paidMort) {
        this.paidMort = paidMort;
    }

    public BigDecimal getHaveMort() {
        return haveMort;
    }

    public void setHaveMort(BigDecimal haveMort) {
        this.haveMort = haveMort;
    }

    public BigDecimal getPaidTim() {
        return paidTim;
    }

    public void setPaidTim(BigDecimal paidTim) {
        this.paidTim = paidTim;
    }

    public BigDecimal getHaveTim() {
        return haveTim;
    }

    public void setHaveTim(BigDecimal haveTim) {
        this.haveTim = haveTim;
    }

    public BigDecimal getPaidInter() {
        return paidInter;
    }

    public void setPaidInter(BigDecimal paidInter) {
        this.paidInter = paidInter;
    }

    public BigDecimal getHaveInter() {
        return haveInter;
    }

    public void setHaveInter(BigDecimal haveInter) {
        this.haveInter = haveInter;
    }

    public Integer getMonths() {
        return months;
    }

    public void setMonths(Integer months) {
        this.months = months;
    }

    public Integer getStatecode() {
        return statecode;
    }

    public void setStatecode(Integer statecode) {
        this.statecode = statecode;
    }

    public Date getPaidTimDate() {
        return paidTimDate;
    }

    public void setPaidTimDate(Date paidTimDate) {
        this.paidTimDate = paidTimDate;
    }

    public Date getHaveTimDate() {
        return haveTimDate;
    }

    public void setHaveTimDate(Date haveTimDate) {
        this.haveTimDate = haveTimDate;
    }

    public Date getPaidInterDate() {
        return paidInterDate;
    }

    public void setPaidInterDate(Date paidInterDate) {
        this.paidInterDate = paidInterDate;
    }

    public Date getHaveInterDate() {
        return haveInterDate;
    }

    public void setHaveInterDate(Date haveInterDate) {
        this.haveInterDate = haveInterDate;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "RepayPlan{" +
        "id=" + id +
        ", projectId=" + projectId +
        ", joininId=" + joininId +
        ", memberId=" + memberId +
        ", lendAmount=" + lendAmount +
        ", paidMort=" + paidMort +
        ", haveMort=" + haveMort +
        ", paidTim=" + paidTim +
        ", haveTim=" + haveTim +
        ", paidInter=" + paidInter +
        ", haveInter=" + haveInter +
        ", months=" + months +
        ", statecode=" + statecode +
        ", paidTimDate=" + paidTimDate +
        ", haveTimDate=" + haveTimDate +
        ", paidInterDate=" + paidInterDate +
        ", haveInterDate=" + haveInterDate +
        "}";
    }
}

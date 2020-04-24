package com.xinshang.rest.modular.asmall.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 供应商资金流水
 * </p>
 *
 * @author zhoushuai
 * @since 2019-10-27
 */
@TableName("tb_supplier_log_moneys")
public class SupplierLogMoneys extends Model<SupplierLogMoneys> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 供应商id
     */
    @TableField("supplier_id")
    private Integer supplierId;
    /**
     * 供应商手机号
     */
    private String phone;
    /**
     * 金额（元）
     */
    private BigDecimal money;
    /**
     * 流水来源：1：充值 2：投资 3：提现
     */
    @TableField("data_src")
    private Integer dataSrc;
    /**
     * 流水标志：1：收入 2：支出
     */
    @TableField("money_type")
    private Integer moneyType;
    /**
     * 通联流水号
     */
    @TableField("trade_no")
    private String tradeNo;
    /**
     * 有效状态 ：1：有效 0：删除
     */
    @TableField("delete_flg")
    private Integer deleteFlg;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 流水备注
     */
    private String remark;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getDataSrc() {
        return dataSrc;
    }

    public void setDataSrc(Integer dataSrc) {
        this.dataSrc = dataSrc;
    }

    public Integer getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(Integer moneyType) {
        this.moneyType = moneyType;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Integer getDeleteFlg() {
        return deleteFlg;
    }

    public void setDeleteFlg(Integer deleteFlg) {
        this.deleteFlg = deleteFlg;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SupplierLogMoneys{" +
        "id=" + id +
        ", supplierId=" + supplierId +
        ", phone=" + phone +
        ", money=" + money +
        ", dataSrc=" + dataSrc +
        ", moneyType=" + moneyType +
        ", tradeNo=" + tradeNo +
        ", deleteFlg=" + deleteFlg +
        ", createTime=" + createTime +
        ", remark=" + remark +
        "}";
    }
}

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
 * 订单退货记录表
 * </p>
 *
 * @author daijunye
 * @since 2019-10-23
 */
@TableName("tb_order_back")
public class OrderBack extends Model<OrderBack> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 订单号
     */
    @TableField("order_id")
    private String orderId;
    /**
     * 状态
     */
    @TableField("state")
    private Integer state;
    /**
     * 操作人
     */
    @TableField("operator_user")
    private String operatorUser;
    /**
     * 操作时间
     */
    @TableField("operator_time")
    private Date operatorTime;
    /**
     * 回退原因
     */
    @TableField("back_remark")
    private String backRemark;
    /**
     * 审核人
     */
    @TableField("audit_user")
    private String auditUser;
    /**
     * 审核时间
     */
    @TableField("audit_time")
    private Date auditTime;
    /**
     * 拒绝原因
     */
    @TableField("refuse_remark")
    private String refuseRemark;
    /**
     * 退货订单号
     */
    @TableField("back_order_id")
    private String backOrderId;
    /**
     * 退货地址
     */
    @TableField("back_address")
    private String backAddress;
    /**
     * 退货商家
     */
    @TableField("back_seller")
    private String backSeller;
    /**
     * 退货电话
     */
    @TableField("back_seller_phone")
    private String backSellerPhone;
    /**
     * 订单金额
     */
    @TableField(exist = false)
    private BigDecimal payAmount;
    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }
    /**
     * 运费
     */
    @TableField(exist = false)
    private BigDecimal freight;
    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }
    /**
     * 退款金额
     */
    @TableField("back_money")
    private BigDecimal backMoney;
    public BigDecimal getBackMoney() {
        return backMoney;
    }

    public void setBackMoney(BigDecimal backMoney) {
        this.backMoney = backMoney;
    }
    /**
     * 退货物流编号
     */
    @TableField(exist = false)
    private String backCompanyCode;
    public String getBackCompanyCode() {
        return backCompanyCode;
    }

    public void setBackCompanyCode(String backCompanyCode) {
        this.backCompanyCode = backCompanyCode;
    }
    /**
     * 退货物流公司名
     */
    @TableField(exist = false)
    private String backCompanyName;
    public String getBackCompanyName() {
        return backCompanyName;
    }

    public void setBackCompanyName(String backCompanyName) {
        this.backCompanyName = backCompanyName;
    }
    /**
     * 商品标题
     */
    @TableField(exist = false)
    private String title;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * 真实姓名
     */
    @TableField(exist = false)
    private String realName;
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
    /**
     * 用户名
     */
    @TableField(exist = false)
    private String username;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * 电话
     */
    @TableField(exist = false)
    private String phone;
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    /**
     * 昵称
     */
    @TableField(exist = false)
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getOperatorUser() {
        return operatorUser;
    }

    public void setOperatorUser(String operatorUser) {
        this.operatorUser = operatorUser;
    }

    public Date getOperatorTime() {
        return operatorTime;
    }

    public void setOperatorTime(Date operatorTime) {
        this.operatorTime = operatorTime;
    }

    public String getBackRemark() {
        return backRemark;
    }

    public void setBackRemark(String backRemark) {
        this.backRemark = backRemark;
    }

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getRefuseRemark() {
        return refuseRemark;
    }

    public void setRefuseRemark(String refuseRemark) {
        this.refuseRemark = refuseRemark;
    }

    public String getBackOrderId() {
        return backOrderId;
    }

    public void setBackOrderId(String backOrderId) {
        this.backOrderId = backOrderId;
    }

    public String getBackAddress() {
        return backAddress;
    }

    public void setBackAddress(String backAddress) {
        this.backAddress = backAddress;
    }

    public String getBackSeller() {
        return backSeller;
    }

    public void setBackSeller(String backSeller) {
        this.backSeller = backSeller;
    }

    public String getBackSellerPhone() {
        return backSellerPhone;
    }

    public void setBackSellerPhone(String backSellerPhone) {
        this.backSellerPhone = backSellerPhone;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "OrderBack{" +
        "id=" + id +
        ", orderId=" + orderId +
        ", state=" + state +
        ", operatorUser=" + operatorUser +
        ", operatorTime=" + operatorTime +
        ", backRemark=" + backRemark +
        ", auditUser=" + auditUser +
        ", auditTime=" + auditTime +
        ", refuseRemark=" + refuseRemark +
        ", backOrderId=" + backOrderId +
        ", backAddress=" + backAddress +
        ", backSeller=" + backSeller +
        ", backSellerPhone=" + backSellerPhone +
        "}";
    }
}

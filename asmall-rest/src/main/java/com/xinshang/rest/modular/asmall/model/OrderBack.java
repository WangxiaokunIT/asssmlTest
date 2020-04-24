package com.xinshang.rest.modular.asmall.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author daijunye
 * @since 2019-10-17
 */
@TableName("tb_order_back")
@Data
@ApiModel("订单退货信息")
public class OrderBack extends Model<OrderBack> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "订单退货id",required=true)
    private Integer id;
    /**
     * 订单号
     */
    @TableField("order_id")
    @ApiModelProperty(value = "订单ID")
    private String orderId;
    /**
     * 状态
     */
    @TableField("state")
    @ApiModelProperty(value = "状态")
    private Integer state;
    /**
     * 操作人
     */
    @TableField("operator_user")
    @ApiModelProperty(value = "操作人")
    private String operatorUser;
    /**
     * 操作时间
     */
    @TableField("operator_time")
    @ApiModelProperty(value = "操作时间")
    private Date operatorTime;
    /**
     * 回退原因
     */
    @TableField("back_remark")
    @ApiModelProperty(value = "回退原因")
    private String backRemark;
    /**
     * 审核人
     */
    @TableField("audit_user")
    @ApiModelProperty(value = "审核人")
    private String auditUser;
    /**
     * 审核时间
     */
    @TableField("audit_time")
    @ApiModelProperty(value = "审核时间")
    private Date auditTime;
    /**
     * 拒绝原因
     */
    @TableField("refuse_remark")
    @ApiModelProperty(value = "拒绝原因")
    private String refuseRemark;
    /**
     * 退货订单号
     */
    @TableField("back_order_id")
    @ApiModelProperty(value = "退货订单号")
    private String backOrderId;
    /**
     * 退货地址
     */
    @TableField("back_address")
    @ApiModelProperty(value = "退货地址")
    private String backAddress;
    /**
     * 退货商家
     */
    @TableField("back_seller")
    @ApiModelProperty(value = "退货商家")
    private String backSeller;
    /**
     * 退货物流编号
     */
    @TableField("back_company_code")
    private String backCompanyCode;
    /**
     * 退货电话
     */
    @TableField("back_seller_phone")
    @ApiModelProperty(value = "退货电话")
    private String backSellerPhone;
    /**
     * 退款金额
     */
    @TableField("back_money")
    @ApiModelProperty(value = "退款金额")
    private BigDecimal backMoney;


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

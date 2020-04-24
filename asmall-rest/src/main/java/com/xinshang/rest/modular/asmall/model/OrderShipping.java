package com.xinshang.rest.modular.asmall.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单地址表
 * </p>
 *
 * @author wangxiaokun
 * @since 2019-10-17
 */
@TableName("tb_order_shipping")
@ApiModel("订单地址信息")
@Data
public class OrderShipping extends Model<OrderShipping> {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @TableId("order_id")
    @ApiModelProperty(value = "订单ID",required=true)
    private String orderId;
    /**
     * 收货人全名
     */
    @TableField("receiver_name")
    @ApiModelProperty(value = "收货人全名")
    private String receiverName;
    /**
     * 固定电话
     */
    @TableField("receiver_phone")
    @ApiModelProperty(value = "固定电话")
    private String receiverPhone;
    /**
     * 移动电话
     */
    @TableField("receiver_mobile")
    @ApiModelProperty(value = "移动电话")
    private String receiverMobile;
    /**
     * 省份
     */
    @TableField("receiver_state")
    @ApiModelProperty(value = "省份")
    private String receiverState;
    /**
     * 城市
     */
    @TableField("receiver_city")
    @ApiModelProperty(value = "城市")
    private String receiverCity;
    /**
     * 区/县
     */
    @TableField("receiver_district")
    @ApiModelProperty(value = "区/县")
    private String receiverDistrict;
    /**
     * 收货地址，如：xx路xx号
     */
    @TableField("receiver_address")
    @ApiModelProperty(value = "收货地址")
    private String receiverAddress;
    /**
     * 邮政编码,如：310001
     */
    @TableField("receiver_zip")
    @ApiModelProperty(value = "邮政编码")
    private String receiverZip;
    private Date created;
    private Date updated;


    @Override
    protected Serializable pkVal() {
        return this.orderId;
    }

    @Override
    public String toString() {
        return "OrderShipping{" +
        "orderId=" + orderId +
        ", receiverName=" + receiverName +
        ", receiverPhone=" + receiverPhone +
        ", receiverMobile=" + receiverMobile +
        ", receiverState=" + receiverState +
        ", receiverCity=" + receiverCity +
        ", receiverDistrict=" + receiverDistrict +
        ", receiverAddress=" + receiverAddress +
        ", receiverZip=" + receiverZip +
        ", created=" + created +
        ", updated=" + updated +
        "}";
    }
}

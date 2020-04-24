package com.xinshang.rest.modular.asmall.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author wangxiaokun
 * @since 2019-10-17
 */
@TableName("tb_order")
@Data
@ApiModel("订单信息")
public class Order extends Model<Order> {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId("order_id")
    @ApiModelProperty(value = "订单ID",required=true)
    private String orderId;

    /**
     * 邮费
     */
    @TableField("post_fee")
    @ApiModelProperty(value = "邮费（后台维护）")
    private BigDecimal postFee;
    /**
     * 状态 0全部 1待付款 2待收货 3已完成 4已取消 5已结束 6退货中 7退货完成 8待发货
     */
    @ApiModelProperty(value = "订单状态")
    private Integer status;
    /**
     * 订单创建时间
     */
    @TableField("create_time")
    @ApiModelProperty(value = "订单创建时间")
    private Date createTime;
    /**
     * 订单更新时间
     */
    @TableField("update_time")
    @ApiModelProperty(value = "订单更新时间")
    private String updateTime;
    /**
     * 发货时间
     */
    @TableField("consign_time")
    @ApiModelProperty(value = "发货时间")
    private String consignTime;
    /**
     * 交易完成时间
     */
    @TableField("end_time")
    @ApiModelProperty(value = "交易完成时间")
    private Date endTime;
    /**
     * 交易关闭时间
     */
    @TableField("close_time")
    @ApiModelProperty(value = "交易关闭时间")
    private String closeTime;

    /**
     * 物流名称
     */
    @TableField("shipping_name")
    @ApiModelProperty(value = "物流名称")
    private String shippingName;

    /**
     * 物流公司ID
     */
    @TableField("shipping_id")
    private String shippingId;
    /**
     * 物流单号
     */
    @TableField("shipping_code")
    @ApiModelProperty(value = "物流单号")
    private String shippingCode;
    /**
     * 用户id
     */
    @TableField("user_id")
    @ApiModelProperty(value = "用户id")
    private Long userId;
    /**
     * 买家留言
     */
    @TableField("buyer_message")
    @ApiModelProperty(value = "买家留言")
    private String buyerMessage;
    /**
     * 买家昵称
     */
    @TableField("buyer_nick")
    @ApiModelProperty(value = "买家昵称")
    private String buyerNick;
    /**
     * 买家是否已经评价
     */
    @TableField("buyer_comment")
    @ApiModelProperty(value = "买家是否已经评价")
    private Integer buyerComment;
    /**
     * 订单金额
     */
    @TableField("pay_amount")
    @ApiModelProperty(value = "订单金额")
    private BigDecimal payAmount;
    /**
     * 订单利润
     */
    @TableField("profits_goods")
    @ApiModelProperty(value = "订单利润")
    private BigDecimal profitsGoods;

    /**
     * 订单类型[0: 普通购买,1:参加拼团,2:会员服务,3:兑换商品]
     */
    @TableField("custom_type")
    @ApiModelProperty(value = "订单类型")
    private Integer customType;

    /**
     * 支付金额
     */
    @ApiModelProperty(value = "支付金额")
    private BigDecimal payment;
    /**
     * 支付时间
     */
    @TableField("payment_time")
    @ApiModelProperty(value = "支付时间")
    private Date paymentTime;
    /**
     * 支付编号
     */
    @TableField("pay_num")
    @ApiModelProperty(value = "支付编号")
    private String payNum;
    /**
     * 支付方式payType 1 收银宝网关支付 2收银宝快捷支付 3 微信JS支付
     */
    @TableField("payment_type")
    @ApiModelProperty(value = "支付类型")
    private Integer paymentType;
    /**
     * 退款编号[微信退款需要，支付宝退款无需生成退款编号]
     */
    @TableField("refund_num")
    @ApiModelProperty(value = "退款编号")
    private String refundNum;

    /**
     * app是否显示[0不显示，1显示]
     */
    @TableField("is_hidden")
    @ApiModelProperty(value = "app是否显示[0不显示，1显示]")
    private Integer isHidden;
    /**
     * 邮费
     */
    @TableField("freight")
    @ApiModelProperty(value = "运费")
    private BigDecimal freight;

    /**
     * 订单创建开始时间
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "订单创建开始时间")
    private String minDateCreate;

    /**
     * 订单创建结束时间
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "订单创建结束时间")
    private String maxDateCreate;

    /**
     * 订单结束时间开始
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "订单结束时间开始")
    private String minDateEnd;
    /**
     * 订单结束时间结束
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "订单结束时间结束")
    private String maxDateEnd;

    /**
     * 商品id和数量
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "商品id和数量")
    private List<CartProduct> goodsList;

    @TableField(exist = false)
    @ApiModelProperty(value = "订单生效剩余时间 分钟")
    private Integer orderRemainingMinutes;

    @TableField(exist = false)
    @ApiModelProperty(value = "订单生效剩余时间 秒")
    private Integer orderRemainingSeconds;

    @TableField(exist = false)
    @ApiModelProperty(value = "订单地址")
    private OrderShipping addressInfo;


    @Override
    protected Serializable pkVal() {
        return null;
    }

    /**
     * 退货数据
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "退货数据")
    private OrderBack orderBack;
}

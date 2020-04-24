package com.xinshang.modular.biz.model;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xinshang.core.annotation.DictField;
import lombok.Data;

import java.io.Serializable;

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
public class Order extends Model<Order> {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId("order_id")
    private String orderId;
    /**
     * 邮费
     */
    @TableField("post_fee")
    private BigDecimal postFee;
    /**
     * 状态 0全部 1待付款 2待收货 3已完成 4已取消 5已结束 6退货中 7退货完成 8待发货
     */
    @DictField("biz_orderStatus")
    private Integer status;

    /**
     * 发货时间
     */
    @TableField("consign_time")
    private Date consignTime;
    /**
     * 交易完成时间
     */
    @TableField("end_time")
    private Date endTime;
    /**
     * 交易关闭时间
     */
    @TableField("close_time")
    private Date closeTime;

    /**
     * 物流公司代号
     */
    @TableField(exist = false)
    private Integer express;
    /**
     * 物流名称
     */
    @TableField("shipping_name")
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
    private String shippingCode;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 买家留言
     */
    @TableField("buyer_message")
    private String buyerMessage;
    /**
     * 买家昵称
     */
    @TableField("buyer_nick")
    private String buyerNick;
    /**
     * 买家是否已经评价
     */
    @TableField("buyer_comment")
    private Integer buyerComment;
    /**
     * 订单金额
     */
    @TableField("pay_amount")
    private BigDecimal payAmount;
    /**
     * 订单利润
     */
    @TableField("profits_goods")
    private BigDecimal profitsGoods;

    /**
     * [0: 普通订单,3:兑换订单]
     */
    @TableField("custom_type")
    @DictField("order_type")
    private Integer customType;

    /**
     * 支付金额
     */
    private BigDecimal payment;
    /**
     * 支付时间
     */
    @TableField("payment_time")
    private Date paymentTime;
    /**
     * 支付编号
     */
    @TableField("pay_num")
    private String payNum;
    /**
     * 支付类型 1通联网关支付 2通联快捷支付 3 微信公众号支付
     */
    @TableField("payment_type")
    private Integer paymentType;
    /**
     * 退款编号[微信退款需要，支付宝退款无需生成退款编号]
     */
    @TableField("refund_num")
    private String refundNum;
    /**
     * 运费
     */
    @TableField("freight")
    private BigDecimal freight;

    /**
     * 订单创建开始时间
     */
    @TableField(exist = false)
    private String minDateCreate;

    /**
     * 订单创建结束时间
     */
    @TableField(exist = false)
    private String maxDateCreate;

    /**
     * 订单结束时间开始
     */
    @TableField(exist = false)
    private String minDateEnd;
    /**
     * 订单结束时间结束
     */
    @TableField(exist = false)
    private String maxDateEnd;

    /**
     * 订单创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 订单更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 真实姓名
     */
    @TableField(exist = false)
    private String realName;

    /**
     * 用户名
     */
    @TableField(exist = false)
    private String username;

    /**
     * 电话
     */
    @TableField(exist = false)
    private String phone;

    /**
     * 昵称
     */
    @TableField(exist = false)
    private String nickname;
    @Override
    protected Serializable pkVal() {
        return null;
    }
}

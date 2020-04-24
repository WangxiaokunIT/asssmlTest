package com.xinshang.rest.modular.asmall.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.xinshang.rest.factory.PageFactory;
import com.xinshang.rest.modular.asmall.model.CartProduct;
import com.xinshang.rest.modular.asmall.model.Item;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhangjiajia
 */
@Data
public class OrderDTO extends PageFactory {

    /**
     * 客户id
     */
    private String userId;

    /**
     * 订单状态
     */
    private Integer status;
    /**
     * 地址id
     */
    private Long addressId;
    /**
     * 客户电话
     */
    private String tel;
    /**
     * 客户名称
     */
    private String userName;
    /**
     * 街道名称
     */
    private String streetName;

    /**
     *   应付金额
     */
    private BigDecimal orderTotal;

    /**
     * [0: 普通购买,1:参加拼团,2:会员服务,3:兑换商品]
     */
    private Integer customType;

    /**
     *  收货地址
     */
    private String address;

    /**
     *  收货人姓名
     */
    private String consignee;

    /**
     *  收货人手机号
     */
    private String phone;

    /**
     * 要购买的商品id和数量
     */
    private List<CartProduct> goodsList;

    /**
     * 要购买的商品(数据库查询出来)
     */
    private List<Item> items;


    //买家留言【备注】
    private String buyerMessage;

    //订单编号
    private String orderId;

    //邮费
    private Integer freight;

    //微信支付必传参数
    private String openId;

    //支付方式payType 1 收银宝网关支付 2收银宝快捷支付 3 微信JS支付（公众号）_集团
    private Integer payType;

    //短信验证码【用于后台支付确认】
    private String verificationCode;

    //银行卡id
    private int bankId;

    //订单申请的商户订单号 （支付订单）
    private String bizOrderNo;
    //交易编号
    private String tradeNo;
    //支付金额
    private String payMent;
    //前端+短信验证码接收回调地址
    private String jumpUrl;

    //消费申请确认接口标记
    private Integer consumeType;

}

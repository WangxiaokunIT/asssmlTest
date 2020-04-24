package com.xinshang.rest.common.enums;

/**
 *
 */
public interface OrderConstant {



    /**
     * 普通购买订单开头
     *
     */
    public static String ORDINARY_ORDER_ID_PREFIX="O0";

    /**
     * 参加拼团订单开头
     *
     */
    public static String SPELL_ORDER_ID_PREFIX="O1";

    /**
     * 会员服务订单开头
     *
     */
    public static String VIP_ORDER_ID_PREFIX="O2";

    /**
     * 兑换订单开头
     *
     */
    public static String EXCHANGE_ORDER_ID_PREFIX="O3";


    /**
     * 支付订单开头
     */
    public static String TRADING_ORDER_ID_PREFIX="T0";

    /**
     * 支付退款开头
     */
    public static String REFUND_ORDER_ID_PREFIX="T1";

    /**
     * 支付方式-支付宝
     */
    public static Integer ORDER_PAYMENT_TYPE_ALIPAY=1;

    /**
     * 支付方式-微信支付
     */
    public static Integer ORDER_PAYMENT_TYPE_WECHAT=2;

    /**
     * 普通订单未支付自动取消-超时时间(秒) 默认 30分钟
     */
    public static Long ORDINARY_ORDER_CANCEL_TIMEOUT=86400000L;


    /**
     * 订单超时未收货自动收货-超时时间(毫秒) 默认7天
     */
    public static Long ORDER_COMPLATE_TIMEOUT=604800000L;

    /**
     * 订单已完成超时自动关闭-超时时间(毫秒) 默认7天
     */
    public static Long ORDER_CLOSE_TIMEOUT=604800000L;


}

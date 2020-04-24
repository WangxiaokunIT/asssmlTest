package com.xinshang.rest.common.enums;

/**
 * @author sunha
 * @since 2019/8/417:54
 */
public interface MqMessageConstant {

    /**
     * 拼团完成/超时发送消息开头
     */
    public static String SPELL_COMPLETE_OR_TIMEOUT_MESSAGE_PREFIX ="spell:";

    /**
     * 未支付订单超时改为已取消发送消息开头
     */
    public static String ORDER_NO_PAY_TIMEOUT_MESSAGE_PREFIX ="order:";

    /**
     * 订单超时未收货状态改为已结束发送消息开头
     */
    public static String ORDER_RECEIVING_GOODS_TIMEOUT_MESSAGE_PREFIX ="orderDeliver:";

    /**
     * 订单已完成超时改为已结束发送消息开头
     */
    public static String ORDER_COMPLATE_TIMEOUT_MESSAGE_PREFIX ="orderComplate:";

}

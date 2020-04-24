package com.xinshang.rest.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhangjiajia
 * @since 19-7-28
 */
@Getter
@AllArgsConstructor
public enum OrderTypeEnum {

    普通订单(0),
    拼团订单(1),
    会员服务订单(2),
    兑换订单(3);
    private Integer value;

}

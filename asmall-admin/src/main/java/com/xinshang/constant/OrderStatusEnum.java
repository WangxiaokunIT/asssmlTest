package com.xinshang.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhangjiajia
 * @since 19-7-28
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    待付款(1),
    待收货(2),
    已完成(3),
    已取消(4),
    已结束(5),
    退货中(6),
    退货完成(7),
    待发货(8),
    退货拒绝(9);

    private Integer value;

}

package com.xinshang.modular.biz.dto;

import lombok.Data;

/**\
 * 源托管代收订单付款信息
 * @author lyk
 */
@Data
public class PaymentOrderDTO {
    /**
     * 相关代收交易的“商户订单号”
     */
    private String bizOrderNo;

    /**
     * 金额，单位：分
     * 部分代付时，可以少于或等于托管代收订单金额
     */
    private Long amount;
}

package com.xinshang.modular.biz.dto;

import lombok.Data;

/**\
 * 源托管代收订单付款信息
 * @author lyk
 */
@Data
public class CollectionDTO {
    /**
     *  商户系统用户标识，商户系统中唯一编号。
     */
    private String bizUserId;

    /**
     * 金额，单位：分
     * 部分代付时，可以少于或等于托管代收订单金额
     */
    private Long amount;
}

package com.xinshang.modular.biz.dto;

import lombok.Data;

/**
 * 提现申请查询条件
 * @author lyk
 */
@Data
public class CashOutDTO {

    /**
     * 申请开始时间
     */
    private String startTime;

    /**
     * 申请结束时间
     */
    private String endTime;

    /**
     * 供应商姓名
     */
    private String supplierName;

    /**
     * 状态
     */
    private String state;

    /**
     * 银行卡号
     */
    private String bankName;

    /**
     * 提现人的类型  1客户 2供应商
     */
    private Integer type;

}

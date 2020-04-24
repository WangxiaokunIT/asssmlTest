package com.xinshang.modular.biz.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * @author zhangjiajia
 * 提现申请对象
 */
@Data
public class CashWithdrawalApplyRequestDTO {


    /**
     * 供应商id
     */
    private Integer id;

    /**
     * 银行卡号
     */
    private String bankCardNo;

    /**
     * 提现金额
     */
    private BigDecimal amount;
}

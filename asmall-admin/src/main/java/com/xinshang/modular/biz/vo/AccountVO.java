package com.xinshang.modular.biz.vo;



import com.xinshang.core.annotation.DictField;
import com.xinshang.modular.biz.model.Member;
import lombok.Data;

import java.math.BigDecimal;


/**
 * <p>
 * 账户
 * </p>
 *
 * @author sunhao
 * @since 2019-10-24
 */
@Data
public class AccountVO extends Member {

    /**
     * 是否默认
     */
    @DictField("available_balance")
    private BigDecimal availableBalance;
}

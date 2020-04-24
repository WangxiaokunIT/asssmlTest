package com.xinshang.rest.modular.asmall.service;

import com.baomidou.mybatisplus.service.IService;
 import com.xinshang.rest.modular.asmall.model.Account;

import java.math.BigDecimal;

/**
 * <p>
 * 账号信息 服务类
 * </p>
 *
 * @author zhoushuai
 * @since 2019-10-25
 */
public interface IAccountService extends IService<Account> {
    /**
     * 更改供用商余额
     * @param supplierId
     * @param amount
     * @return
     */
    boolean updateAmount(Long supplierId, BigDecimal amount);

    /**
     * 更改账户余额
     * @param supplierId
     * @param amount
     * @return
     */
    boolean updateMemberAmount(Long supplierId, BigDecimal amount);
}

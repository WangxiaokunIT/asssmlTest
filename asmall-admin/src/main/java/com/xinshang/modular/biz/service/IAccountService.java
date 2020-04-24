package com.xinshang.modular.biz.service;

import com.baomidou.mybatisplus.service.IService;
import com.xinshang.modular.biz.model.Account;
import com.xinshang.modular.biz.model.Joinin;

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


    /**
     * 获取通联余额
     * @param bizUserId
     * @return
     */
    Account getAccount(String bizUserId);
}

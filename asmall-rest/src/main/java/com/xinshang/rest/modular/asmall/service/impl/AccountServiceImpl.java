package com.xinshang.rest.modular.asmall.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import com.xinshang.rest.modular.asmall.dao.AccountMapper;
import com.xinshang.rest.modular.asmall.model.Account;
import com.xinshang.rest.modular.asmall.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <p>
 * 账号信息 服务实现类
 * </p>
 *
 * @author zhoushuai
 * @since 2019-10-25
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

    @Autowired
    private AccountMapper accountMapper;
    @Transactional
    @Override
    public boolean updateAmount(Long supplierId, BigDecimal amount) {
        return accountMapper.updateAmount(supplierId,amount);
    }

    /**
     * 更改客户余额
     * @param supplierId
     * @param amount
     * @return
     */
    @Override
    public boolean updateMemberAmount(Long supplierId, BigDecimal amount) {
        return accountMapper.updateMemberAmount(supplierId,amount);
    }
}

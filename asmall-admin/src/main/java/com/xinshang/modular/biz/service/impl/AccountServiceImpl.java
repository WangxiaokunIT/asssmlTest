package com.xinshang.modular.biz.service.impl;

import com.allinpay.yunst.sdk.bean.YunRequest;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.config.properties.AllinPayProperties;
import com.xinshang.constant.Constants;
import com.xinshang.core.util.AllinPayUtil;
import com.xinshang.modular.biz.dao.AccountMapper;
import com.xinshang.modular.biz.dto.AllinPayQueryAccountDTO;
import com.xinshang.modular.biz.dto.AllinPayResponseDTO;
import com.xinshang.modular.biz.model.Account;
import com.xinshang.modular.biz.service.IAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * <p>
 * 账号信息 服务实现类
 * </p>
 *
 * @author zhoushuai
 * @since 2019-10-25
 */
@Service
@AllArgsConstructor
@Slf4j
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

    private final AccountMapper accountMapper;
    private final AllinPayProperties allinPayProperties;

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

    /**
     * 获取通联账户余额
     * @param bizUserId
     * @return
     */
    @Override
    public Account getAccount(String bizUserId) {
        log.info("获取通联账户余额:{}", bizUserId);
        YunRequest request = new YunRequest("OrderService","queryBalance");
        request.put("bizUserId", bizUserId);
        request.put("accountSetNo", allinPayProperties.getAccountSetNo());

        Optional<AllinPayResponseDTO<AllinPayQueryAccountDTO>> response = AllinPayUtil.request(request, AllinPayQueryAccountDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(Constants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
            log.warn("查询账户信息通联审核结果失败:{}", response);
            return response.get().getMessage();
        });

        AllinPayQueryAccountDTO signedValue = response.get().getSignedValue();
        Account account = new Account();
        BigDecimal totleAmoun = new BigDecimal((signedValue.getAllAmount() / 100.0) + "");
        BigDecimal freezenAmount = new BigDecimal((signedValue.getFreezenAmount() / 100.0) + "");
        account.setTotleAmount(totleAmoun);
        account.setAvailableBalance(totleAmoun.subtract(freezenAmount));
        account.setFreezingAmount(freezenAmount);
        return account;
    }


}

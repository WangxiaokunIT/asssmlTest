package com.xinshang.rest.modular.asmall.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xinshang.rest.modular.asmall.model.Bank;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 银行卡信息 Mapper 接口
 * </p>
 *
 * @author sunhao
 * @since 2019-10-24
 */
@Repository
public interface BankMapper extends BaseMapper<Bank> {
    /**
     * 获取个人绑定银行卡信息
     * @param memberId
     * @return
     */
    Bank selectByMemberId (Long memberId);
}

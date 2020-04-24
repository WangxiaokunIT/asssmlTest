package com.xinshang.rest.modular.asmall.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xinshang.rest.modular.asmall.model.Account;
 import com.xinshang.rest.modular.asmall.model.Account;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 账号信息 Mapper 接口
 * </p>
 *
 * @author zhoushuai
 * @since 2019-10-25
 */
public interface AccountMapper extends BaseMapper<Account> {

    Boolean  updateAmount(@Param("supplierId") Long supplierId, @Param("amount") BigDecimal amount);

    Boolean  updateMemberAmount(@Param("supplierId") Long supplierId, @Param("amount") BigDecimal amount);
}

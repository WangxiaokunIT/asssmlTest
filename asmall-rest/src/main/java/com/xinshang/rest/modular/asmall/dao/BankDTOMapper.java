package com.xinshang.rest.modular.asmall.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xinshang.rest.modular.asmall.dto.BankDTO;
import com.xinshang.rest.modular.asmall.model.Bank;

/**
 * <p>
 * 银行卡信息 Mapper 接口
 * </p>
 *
 * @author sunhao
 * @since 2019-10-24
 */
public interface BankDTOMapper extends BaseMapper<BankDTO> {

    BankDTO selectMembeById(Long id);
}

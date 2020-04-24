package com.xinshang.rest.modular.asmall.service;

import com.baomidou.mybatisplus.service.IService;
import com.xinshang.rest.modular.asmall.dto.BankDTO;

/**
 * <p>
 * 银行卡信息 服务类
 * </p>
 *
 * @author sunhao
 * @since 2019-10-24
 */
public interface IBankService extends IService<BankDTO> {
    BankDTO selectMembeById(Long id);
}

package com.xinshang.modular.biz.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.modular.biz.model.Bank;
import com.baomidou.mybatisplus.service.IService;
import com.xinshang.modular.biz.vo.BankVO;

/**
 * <p>
 * 银行卡信息 服务类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-11-12
 */
public interface IBankService extends IService<Bank> {

    /**
     * 查询银行卡列表
     * @param masterId
     * @param type
     * @param page
     * @return
     */
    Page<BankVO> selectBankInfo(Integer masterId, Integer type, Page<BankVO> page);
}

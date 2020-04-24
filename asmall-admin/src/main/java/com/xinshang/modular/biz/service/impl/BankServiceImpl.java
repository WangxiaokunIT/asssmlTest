package com.xinshang.modular.biz.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.modular.biz.model.Bank;
import com.xinshang.modular.biz.dao.BankMapper;
import com.xinshang.modular.biz.service.IBankService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.modular.biz.vo.BankVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 银行卡信息 服务实现类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-11-12
 */
@Service
@AllArgsConstructor
public class BankServiceImpl extends ServiceImpl<BankMapper, Bank> implements IBankService {

    private final BankMapper bankMapper;
    /**
     * 查询银行卡列表
     * @param masterId
     * @param type
     * @param page
     */
    @Override
    public Page<BankVO> selectBankInfo(Integer masterId, Integer type, Page<BankVO> page) {
        List<BankVO> bankVOS = bankMapper.selectBankInfo(masterId, type, page);
        page.setRecords(bankVOS);
        return page;
    }
}

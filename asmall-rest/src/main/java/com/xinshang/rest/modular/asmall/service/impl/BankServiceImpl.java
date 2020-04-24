package com.xinshang.rest.modular.asmall.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.rest.modular.asmall.dao.BankDTOMapper;
import com.xinshang.rest.modular.asmall.dto.BankDTO;
import com.xinshang.rest.modular.asmall.service.IBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 银行卡信息 服务实现类
 * </p>
 *
 * @author sunhao
 * @since 2019-10-24
 */
@Service
public class BankServiceImpl extends ServiceImpl<BankDTOMapper, BankDTO> implements IBankService {


    @Autowired
    private BankDTOMapper bankDTOMapper;


    @Override
    public BankDTO selectMembeById(Long id) {
        return bankDTOMapper.selectMembeById(id);
    }
}

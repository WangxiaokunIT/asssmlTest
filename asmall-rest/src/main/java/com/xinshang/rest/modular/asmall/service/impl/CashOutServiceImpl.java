package com.xinshang.rest.modular.asmall.service.impl;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import com.xinshang.rest.modular.asmall.dao.CashOutMapper;
import com.xinshang.rest.modular.asmall.dto.CashOutDTO;
import com.xinshang.rest.modular.asmall.model.CashOut;
 import com.xinshang.rest.modular.asmall.service.ICashOutService;
 import com.xinshang.rest.modular.asmall.vo.CashOutVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


 import java.util.List;

/**
 * <p>
 * 提现审核 服务实现类
 * </p>
 *
 * @author lvyingkai
 * @since 2019-10-22
 */
@Slf4j
@Service
@AllArgsConstructor
public class CashOutServiceImpl extends ServiceImpl<CashOutMapper, CashOut> implements ICashOutService {

    private final CashOutMapper cashOutMapper;


    @Override
    public List<CashOutVO> showCashOut(CashOutDTO param, Page<CashOutVO> page) {
        return cashOutMapper.showCashOut(param, page);
    }




}

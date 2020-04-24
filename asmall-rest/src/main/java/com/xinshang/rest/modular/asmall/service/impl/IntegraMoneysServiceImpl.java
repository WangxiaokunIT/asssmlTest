package com.xinshang.rest.modular.asmall.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.rest.modular.asmall.dao.ClientLogMoneysMapper;
import com.xinshang.rest.modular.asmall.dao.IntergraLogMoneysMapper;
import com.xinshang.rest.modular.asmall.dto.LogDTO;
import com.xinshang.rest.modular.asmall.model.ClientLogMoneys;
import com.xinshang.rest.modular.asmall.model.IntegralLogMoneys;
import com.xinshang.rest.modular.asmall.service.IClientLogMoneysService;
import com.xinshang.rest.modular.asmall.service.IIntegralLogMoneysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 * 加盟积分表 服务实现类
 * </p>
 *
 * @author zhoushuai
 * @since 2019-10-24
 */
@Service
public class IntegraMoneysServiceImpl extends ServiceImpl<IntergraLogMoneysMapper, IntegralLogMoneys> implements IIntegralLogMoneysService {
    @Autowired
    private IntergraLogMoneysMapper intergraLogMoneysMapper;
    @Override
    public List<IntegralLogMoneys> showIntergraLog(LogDTO param, Page<IntegralLogMoneys> page) {
        return intergraLogMoneysMapper.showIntergraLog(param, page);
    }

}

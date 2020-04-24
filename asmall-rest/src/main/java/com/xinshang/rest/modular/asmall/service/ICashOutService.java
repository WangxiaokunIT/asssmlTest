package com.xinshang.rest.modular.asmall.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import com.xinshang.rest.modular.asmall.dto.CashOutDTO;
import com.xinshang.rest.modular.asmall.model.CashOut;
import com.xinshang.rest.modular.asmall.vo.CashOutVO;

import java.util.List;

/**
 * <p>
 * 提现审核 服务类
 * </p>
 *
 * @author lvyingkai
 * @since 2019-10-22
 */
public interface ICashOutService extends IService<CashOut> {

    /**
     * 根据查询条件获取审核信息
     * @param param
     * @param page
     * @return
     */
    List<CashOutVO> showCashOut(CashOutDTO param, Page<CashOutVO> page);



}

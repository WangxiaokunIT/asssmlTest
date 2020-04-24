package com.xinshang.rest.modular.asmall.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.xinshang.rest.modular.asmall.dto.LogDTO;
import com.xinshang.rest.modular.asmall.dto.ReceiptsPayments;
import com.xinshang.rest.modular.asmall.model.ClientLogMoneys;
import java.util.List;

/**
 * <p>
 * 客户资金流水服务类
 * </p>
 *
 * @author jyz
 * @since 2019-10-25
 */
public interface ILogService extends IService<ClientLogMoneys> {


    /**
     * 获取客户资金流水列表
     * @param param
     * @return
     */
    Page<ReceiptsPayments> showClientLog(LogDTO param);





}

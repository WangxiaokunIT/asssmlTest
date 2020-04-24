package com.xinshang.rest.modular.asmall.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.xinshang.rest.modular.asmall.dto.LogDTO;
import com.xinshang.rest.modular.asmall.model.ClientLogMoneys;
import com.xinshang.rest.modular.asmall.model.IntegralLogMoneys;

import java.util.List;

/**
 * <p>
 * 加盟积分表 服务类
 * </p>
 *
 * @author zhoushuai
 * @since 2019-10-19
 */
public interface IIntegralLogMoneysService extends IService<IntegralLogMoneys> {
    /**
     * 获取金粉流水列表
     * @param param
     * @param page
     * @return
     */
    List<IntegralLogMoneys> showIntergraLog(LogDTO param, Page<IntegralLogMoneys> page);
}

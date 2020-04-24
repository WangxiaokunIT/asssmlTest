package com.xinshang.modular.biz.service;

import com.baomidou.mybatisplus.service.IService;
import com.xinshang.modular.biz.model.EqbContract;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-10-23
 */
public interface IEqbContractService extends IService<EqbContract> {
    /**
     * 创建合同
     * @param projectId
     * @return
     */
    boolean createContract(Long projectId);
}

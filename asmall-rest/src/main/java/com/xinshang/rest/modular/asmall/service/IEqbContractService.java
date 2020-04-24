package com.xinshang.rest.modular.asmall.service;

import com.baomidou.mybatisplus.service.IService;
import com.xinshang.rest.common.util.wk.helper.exception.DefineException;
import com.xinshang.rest.config.properties.OssProperties;
import com.xinshang.rest.modular.asmall.dto.CreateContractRequestDTO;
import com.xinshang.rest.modular.asmall.model.EqbContract;
import com.xinshang.rest.modular.asmall.model.EqbDTO;
import com.xinshang.rest.modular.asmall.model.Member;

import java.io.IOException;

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
    EqbContract createContract(CreateContractRequestDTO createContractRequestDTO, Member member);

    Boolean eqbCallBack(EqbDTO eqbDTO, OssProperties ossProperties) throws DefineException, IOException;
}

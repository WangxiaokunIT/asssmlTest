package com.xinshang.rest.modular.asmall.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.rest.modular.asmall.dao.ParameterMapper;
import com.xinshang.rest.modular.asmall.model.Parameter;
import com.xinshang.rest.modular.asmall.service.IParameterService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统参数 服务实现类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-11-26
 */
@Service
public class ParameterServiceImpl extends ServiceImpl<ParameterMapper, Parameter> implements IParameterService {

}

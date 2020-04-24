package com.xinshang.modular.biz.service.impl;

import com.xinshang.modular.biz.model.Parameter;
import com.xinshang.modular.biz.dao.ParameterMapper;
import com.xinshang.modular.biz.service.IParameterService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
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

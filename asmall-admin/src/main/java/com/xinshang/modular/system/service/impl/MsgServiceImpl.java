package com.xinshang.modular.system.service.impl;

import com.xinshang.modular.system.model.Msg;
import com.xinshang.modular.system.dao.MsgMapper;
import com.xinshang.modular.system.service.IMsgService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统消息 服务实现类
 * </p>
 *
 * @author zhangjiajia
 * @since 2018-10-12
 */
@Service
public class MsgServiceImpl extends ServiceImpl<MsgMapper, Msg> implements IMsgService {

}

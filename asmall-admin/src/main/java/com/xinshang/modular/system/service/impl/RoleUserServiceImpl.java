package com.xinshang.modular.system.service.impl;

import com.xinshang.modular.system.model.RoleUser;
import com.xinshang.modular.system.dao.RoleUserMapper;
import com.xinshang.modular.system.service.IRoleUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色用户关联表 服务实现类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-02-13
 */
@Service
public class RoleUserServiceImpl extends ServiceImpl<RoleUserMapper, RoleUser> implements IRoleUserService {

}

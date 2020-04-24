package com.xinshang.modular.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.modular.system.dao.RoleMenuMapper;
import com.xinshang.modular.system.model.RoleMenu;
import com.xinshang.modular.system.service.IRoleMenuService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色和菜单关联表 服务实现类
 * </p>
 *
 * @author zhangjiajia
 * @since 2018-02-22
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

}

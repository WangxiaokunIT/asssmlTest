package com.xinshang.modular.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.constant.Constants;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.core.util.Convert;
import com.xinshang.modular.system.dao.RoleMenuMapper;
import com.xinshang.modular.system.dao.RoleMapper;
import com.xinshang.modular.system.model.RoleMenu;
import com.xinshang.modular.system.model.Role;
import com.xinshang.modular.system.service.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author zhangjiajia
 * @date 2018年11月26日 10:29:25
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setAuthority(Integer roleId, String ids) {

        // 删除该角色所有的权限
        this.roleMapper.deleteRolesById(roleId);

        // 添加新的权限
        for (Long id : Convert.toLongArray(true, Convert.toStrArray(Constants.UNIVERSAL_STRING_SPLITTING_RULES, ids))) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(id);
            this.roleMenuMapper.insert(roleMenu);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delRoleById(Integer roleId) {
        //删除角色
        this.roleMapper.deleteById(roleId);

        // 删除该角色所有的权限
        this.roleMapper.deleteRolesById(roleId);
    }


    @Override
    public int deleteRolesById(Integer roleId) {
        return this.baseMapper.deleteRolesById(roleId);
    }

    @Override
    public List<ZTreeNode> roleTreeList() {
        return this.baseMapper.roleTreeList();
    }

    @Override
    public List<ZTreeNode> roleTreeListByRoleId(Integer[] roleId) {
        return this.baseMapper.roleTreeListByRoleId(roleId);
    }

}

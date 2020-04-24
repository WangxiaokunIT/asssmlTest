package com.xinshang.modular.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.core.node.MenuNode;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.system.dao.MenuMapper;
import com.xinshang.modular.system.model.Menu;
import com.xinshang.core.node.MenuNode;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.system.dao.MenuMapper;
import com.xinshang.modular.system.model.Menu;
import com.xinshang.modular.system.service.IMenuService;
import com.xinshang.core.node.MenuNode;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.system.dao.MenuMapper;
import com.xinshang.modular.system.model.Menu;
import com.xinshang.modular.system.service.IMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 菜单服务
 *
 * @author fengshuonan
 * @date 2017-05-05 22:20
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Resource
    private MenuMapper menuMapper;

    @Override
    public void delMenu(Integer menuId) {

        //删除菜单
        this.menuMapper.deleteById(menuId);

        //删除关联的角色菜单
        this.menuMapper.deleteRoleMenuByMenuId(menuId);
    }

    @Override
    public void delMenuContainSubMenus(Integer menuId) {

        Menu menu = menuMapper.selectById(menuId);
        //删除当前菜单
        delMenu(menuId);

        //删除所有子菜单
        Wrapper<Menu> wrapper = new EntityWrapper<>();
        wrapper = wrapper.like("seq",  "."+menu.getParentId() + ".");
        List<Menu> menus = menuMapper.selectList(wrapper);
        for (Menu temp : menus) {
            delMenu(temp.getId());
        }
    }

    @Override
    public List<Menu> selectMenus(String condition, Integer level) {
        return this.baseMapper.selectMenus(condition, level);
    }

    @Override
    public List<Integer> getMenuIdsByRoleId(Integer roleId) {
        return this.baseMapper.getMenuIdsByRoleId(roleId);
    }

    @Override
    public List<ZTreeNode> menuTreeList() {
        return this.baseMapper.menuTreeList();
    }

    @Override
    public List<ZTreeNode> menuTreeListByMenuIds(List<Integer> menuIds) {
        return this.baseMapper.menuTreeListByMenuIds(menuIds);
    }

    @Override
    public List<MenuNode> getMenusByRoleIds(List<Integer> roleIds) {
        return this.baseMapper.getMenusByRoleIds(roleIds);
    }
}

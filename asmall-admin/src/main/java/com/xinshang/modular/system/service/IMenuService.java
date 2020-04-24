package com.xinshang.modular.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xinshang.core.node.MenuNode;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.system.model.Menu;
import org.apache.ibatis.annotations.Param;
import java.util.List;


/**
 * 菜单服务
 *
 * @author fengshuonan
 * @date 2017-05-05 22:19
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 删除菜单
     * @param menuId
     * @author zhangjiajia
     * @date 2017/5/5 22:20
     */
    void delMenu(Integer menuId);

    /**
     * 删除菜单包含所有子菜单
     * @param menuId
     * @author zhangjiajia
     * @date 2017/6/13 22:02
     */
    void delMenuContainSubMenus(Integer menuId);

    /**
     * 根据条件查询菜单
     * @param condition
     * @param level
     * @return
     * @date 2017年2月12日 下午9:14:34
     */
    List<Menu> selectMenus(@Param("condition") String condition, @Param("level") Integer level);

    /**
     * 根据条件查询菜单
     * @param roleId
     * @return
     * @date 2017年2月12日 下午9:14:34
     */
    List<Integer> getMenuIdsByRoleId(@Param("roleId") Integer roleId);

    /**
     * 获取菜单列表树
     *
     * @return
     * @date 2017年2月19日 下午1:33:51
     */
    List<ZTreeNode> menuTreeList();

    /**
     * 获取菜单列表树
     * @param menuIds
     * @return
     * @date 2017年2月19日 下午1:33:51
     */
    List<ZTreeNode> menuTreeListByMenuIds(List<Integer> menuIds);

    /**
     * 根据角色获取菜单
     *
     * @param roleIds
     * @return
     * @date 2017年2月19日 下午10:35:40
     */
    List<MenuNode> getMenusByRoleIds(List<Integer> roleIds);
}

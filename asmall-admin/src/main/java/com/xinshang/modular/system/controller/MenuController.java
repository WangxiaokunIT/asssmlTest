package com.xinshang.modular.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.base.tips.Tip;
import com.xinshang.core.common.annotion.BussinessLog;
import com.xinshang.core.common.annotion.Permission;
import com.xinshang.core.common.constant.Const;
import com.xinshang.core.common.constant.dictmap.MenuDict;
import com.xinshang.core.common.constant.factory.ConstantFactory;
import com.xinshang.core.common.constant.state.MenuStatus;
import com.xinshang.core.common.constant.state.SystemTreeTableEnum;
import com.xinshang.core.common.exception.BizExceptionEnum;
import com.xinshang.core.exception.SystemException;
import com.xinshang.core.log.LogObjectHolder;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.core.support.BeanKit;
import com.xinshang.core.util.ToolUtil;
import com.xinshang.modular.system.model.Menu;
import com.xinshang.modular.system.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 菜单控制器
 *
 * @author fengshuonan
 * @date 2017年2月12日21:59:14
 */
@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController {

    private static String PREFIX = "/system/menu/";

    @Autowired
    private IMenuService menuService;

    /**
     * 跳转到菜单列表列表页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "menu.html";
    }

    /**
     * 跳转到菜单列表列表页面
     */
    @RequestMapping(value = "/menu_add")
    public String menuAdd() {
        return PREFIX + "menu_add.html";
    }

    /**
     * 跳转到菜单详情列表页面
     */

    @RequestMapping(value = "/menu_edit/{menuId}")
    public String menuEdit(@PathVariable Integer menuId, Model model) {
        if (ToolUtil.isEmpty(menuId)) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }
        Menu menu = this.menuService.selectById(menuId);

        //获取父级菜单的id
        Menu temp = new Menu();
        temp.setId(menu.getParentId());
        Menu parentMenu = this.menuService.selectOne(new EntityWrapper<>(temp));

        //如果父级是顶级菜单
        if (parentMenu == null) {
            menu.setParentId(0);
        } else {
            //设置父级菜单的code为父级菜单的id
            menu.setParentId(parentMenu.getId());
        }

        Map<String, Object> menuMap = BeanKit.beanToMap(menu);
        model.addAttribute("menu", menuMap);
        LogObjectHolder.me().set(menu);
        return PREFIX + "menu_edit.html";
    }

    /**
     * 修该菜单
     */

    @RequestMapping(value = "/edit")
    @BussinessLog(value = "修改菜单", key = "name", dict = MenuDict.class)
    @ResponseBody
    public Tip edit(@Valid Menu menu, BindingResult result) {
        if (result.hasErrors()) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }

        this.menuService.updateById(menu);

         //修改seq和level
        ConstantFactory.me().updateSeqAndLevelById(SystemTreeTableEnum.MENU.getTableName(),menu.getId());

        return SUCCESS_TIP;
    }

    /**
     * 获取菜单列表
     */

    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String menuName, @RequestParam(required = false) Integer level) {
        List<Menu> menus = this.menuService.selectMenus(menuName, level);
        return menus;
    }

    /**
     * 新增菜单
     */

    @RequestMapping(value = "/add")
    @BussinessLog(value = "菜单新增", key = "name", dict = MenuDict.class)
    @ResponseBody
    public Tip add(@Valid Menu menu, BindingResult result) {
        if (result.hasErrors()) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }

        //判断是否存在该编号
        String existedMenuName = ConstantFactory.me().getMenuNameByCode(menu.getCode());
        if (ToolUtil.isNotEmpty(existedMenuName)) {
            throw new SystemException(BizExceptionEnum.EXISTED_THE_MENU.getMessage());
        }

        //设置父级菜单编号
        if (ToolUtil.isEmpty(menu.getParentId())) {
            menu.setParentId(0);
        }

        menu.setState(MenuStatus.ENABLE.getCode());
        this.menuService.insert(menu);
        /**
         * 添加seq和level
         */
        ConstantFactory.me().addSeqAndLevelByIdAndParentId(SystemTreeTableEnum.MENU.getTableName(),menu.getId(),menu.getParentId());

        return SUCCESS_TIP;
    }

    /**
     * 删除菜单
     */

    @RequestMapping(value = "/remove")
    @BussinessLog(value = "删除菜单", key = "menuId", dict = MenuDict.class)
    @ResponseBody
    public Tip remove(@RequestParam Integer menuId) {
        if (ToolUtil.isEmpty(menuId)) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }

        //缓存菜单的名称
        LogObjectHolder.me().set(ConstantFactory.me().getMenuName(menuId));

        this.menuService.delMenuContainSubMenus(menuId);
        return SUCCESS_TIP;
    }

    /**
     * 查看菜单
     */
    @RequestMapping(value = "/view/{menuId}")
    @ResponseBody
    public Tip view(@PathVariable Integer menuId) {
        if (ToolUtil.isEmpty(menuId)) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }
        this.menuService.selectById(menuId);
        return SUCCESS_TIP;
    }

    /**
     * 获取菜单列表(首页用)
     */
    @RequestMapping(value = "/menuTreeList")
    @ResponseBody
    public List<ZTreeNode> menuTreeList() {
        List<ZTreeNode> roleTreeList = this.menuService.menuTreeList();
        return roleTreeList;
    }

    /**
     * 获取菜单列表(选择父级菜单用)
     */
    @RequestMapping(value = "/selectMenuTreeList")
    @ResponseBody
    public List<ZTreeNode> selectMenuTreeList() {
        List<ZTreeNode> roleTreeList = this.menuService.menuTreeList();
        roleTreeList.add(ZTreeNode.createParent());
        return roleTreeList;
    }

    /**
     * 获取角色列表
     */
    @RequestMapping(value = "/menuTreeListByRoleId/{roleId}")
    @ResponseBody
    public List<ZTreeNode> menuTreeListByRoleId(@PathVariable Integer roleId) {
        List<Integer> menuIds = this.menuService.getMenuIdsByRoleId(roleId);
        if (ToolUtil.isEmpty(menuIds)) {
            List<ZTreeNode> roleTreeList = this.menuService.menuTreeList();
            return roleTreeList;
        } else {
            List<ZTreeNode> roleTreeListByUserId = this.menuService.menuTreeListByMenuIds(menuIds);
            return roleTreeListByUserId;
        }
    }


}

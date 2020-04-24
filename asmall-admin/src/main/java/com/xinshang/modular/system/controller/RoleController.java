package com.xinshang.modular.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.base.tips.Tip;
import com.xinshang.core.common.annotion.BussinessLog;
import com.xinshang.core.common.annotion.Permission;
import com.xinshang.core.common.constant.Const;
import com.xinshang.core.common.constant.dictmap.RoleDict;
import com.xinshang.core.common.constant.factory.ConstantFactory;
import com.xinshang.core.common.constant.state.SystemTreeTableEnum;
import com.xinshang.core.common.exception.BizExceptionEnum;
import com.xinshang.core.exception.SystemException;
import com.xinshang.core.log.LogObjectHolder;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.core.util.ToolUtil;
import com.xinshang.modular.system.model.Role;
import com.xinshang.modular.system.model.RoleUser;
import com.xinshang.modular.system.service.IRoleService;
import com.xinshang.modular.system.service.IRoleUserService;
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

/**
 * 角色控制器
 *
 * @author fengshuonan
 * @date 2017年2月12日21:59:14
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    private static String PREFIX = "/system/role";

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IRoleUserService roleUserService;

    /**
     * 跳转到角色列表页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/role.html";
    }

    /**
     * 跳转到添加角色
     */
    @RequestMapping(value = "/role_add")
    public String roleAdd() {
        return PREFIX + "/role_add.html";
    }

    /**
     * 跳转到修改角色
     */

    @RequestMapping(value = "/role_edit/{roleId}")
    public String roleEdit(@PathVariable Integer roleId, Model model) {
        if (ToolUtil.isEmpty(roleId)) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }
        Role role = this.roleService.selectById(roleId);
        model.addAttribute(role);
        LogObjectHolder.me().set(role);
        return PREFIX + "/role_edit.html";
    }

    /**
     * 跳转到角色分配
     */

    @RequestMapping(value = "/role_assign/{roleId}")
    public String roleAssign(@PathVariable("roleId") Integer roleId, Model model) {
        if (ToolUtil.isEmpty(roleId)) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }
        model.addAttribute("roleId", roleId);
        model.addAttribute("roleName", ConstantFactory.me().getSingleRoleName(roleId));
        return PREFIX + "/role_assign.html";
    }

    /**
     * 获取角色列表
     */

    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String roleName) {
        List<Role> list = this.roleService.selectList(new EntityWrapper<Role>().like(StrUtil.isNotBlank(roleName),"name",  roleName ));
        return list;
    }

    /**
     * 角色新增
     */
    @RequestMapping(value = "/add")
    @BussinessLog(value = "添加角色", key = "name", dict = RoleDict.class)

    @ResponseBody
    public Tip add(@Valid Role role, BindingResult result) {
        if (result.hasErrors()) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }

        this.roleService.insert(role);
        /**
         * 添加seq和level
         */
        ConstantFactory.me().addSeqAndLevelByIdAndParentId(SystemTreeTableEnum.ROLE.getTableName(),role.getId(),role.getParentId());
        return SUCCESS_TIP;
    }

    /**
     * 角色修改
     */
    @RequestMapping(value = "/edit")
    @BussinessLog(value = "修改角色", key = "name", dict = RoleDict.class)

    @ResponseBody
    public Tip edit(@Valid Role role, BindingResult result) {
        if (result.hasErrors()) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }
        this.roleService.updateById(role);
        /**
         * 修改seq和level
         */
        ConstantFactory.me().updateSeqAndLevelById(SystemTreeTableEnum.ROLE.getTableName(),role.getId());
        //删除缓存
        return SUCCESS_TIP;
    }

    /**
     * 删除角色
     */
    @RequestMapping(value = "/remove")
    @BussinessLog(value = "删除角色", key = "roleId", dict = RoleDict.class)

    @ResponseBody
    public Tip remove(@RequestParam Integer roleId) {
        if (ToolUtil.isEmpty(roleId)) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }

        //不能删除超级管理员角色
        if (roleId.equals(Const.ADMIN_ROLE_ID)) {
            throw new SystemException(BizExceptionEnum.CANT_DELETE_ADMIN.getMessage());
        }

        //缓存被删除的角色名称
        LogObjectHolder.me().set(ConstantFactory.me().getSingleRoleName(roleId));

        this.roleService.delRoleById(roleId);

        return SUCCESS_TIP;
    }

    /**
     * 查看角色
     */
    @RequestMapping(value = "/view/{roleId}")
    @ResponseBody
    public Tip view(@PathVariable Integer roleId) {
        if (ToolUtil.isEmpty(roleId)) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }
        this.roleService.selectById(roleId);
        return SUCCESS_TIP;
    }

    /**
     * 配置权限
     */
    @RequestMapping("/setAuthority")
    @BussinessLog(value = "配置权限", key = "roleId,ids", dict = RoleDict.class)

    @ResponseBody
    public Tip setAuthority(@RequestParam("roleId") Integer roleId, @RequestParam("ids") String ids) {
        if (ToolUtil.isOneEmpty(roleId)) {
            throw new SystemException(BizExceptionEnum.REQUEST_NULL.getMessage());
        }
        this.roleService.setAuthority(roleId, ids);
        return SUCCESS_TIP;
    }

    /**
     * 获取角色列表
     */
    @RequestMapping(value = "/roleTreeList")
    @ResponseBody
    public List<ZTreeNode> roleTreeList() {
        List<ZTreeNode> roleTreeList = this.roleService.roleTreeList();
        roleTreeList.add(ZTreeNode.createParent());
        return roleTreeList;
    }

    /**
     * 获取角色列表
     */
    @RequestMapping(value = "/roleTreeListByUserId/{userId}")
    @ResponseBody
    public List<ZTreeNode> roleTreeListByUserId(@PathVariable Integer userId) {

        EntityWrapper<RoleUser> wrapper = new EntityWrapper();
        wrapper.eq("user_id",userId);
        List<RoleUser> hasRoles = roleUserService.selectList(wrapper);
        List<ZTreeNode> roleTreeList=null;
        if(hasRoles.size()>0){
            Integer[] roleIds = new Integer[hasRoles.size()];
            for (int i = 0; i < hasRoles.size(); i++) {
                roleIds[i]=hasRoles.get(i).getRoleId();
            }

            roleTreeList = this.roleService.roleTreeListByRoleId(roleIds);
        } else {
            roleTreeList = this.roleService.roleTreeList();
        }
        return roleTreeList;
    }
}

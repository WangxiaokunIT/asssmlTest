package com.xinshang.modular.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.system.model.Role;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * <p>
  * 角色表 Mapper 接口
 * </p>
 *
 * @author zhangjiajia
 * @since 2017-07-11
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 删除某个角色的所有权限
     *
     * @param roleId 角色id
     * @return
     * @date 2017年2月13日 下午7:57:51
     */
    int deleteRolesById(@Param("roleId") Integer roleId);

    /**
     * 获取角色列表树
     *
     * @return
     * @date 2017年2月18日 上午10:32:04
     */
    List<ZTreeNode> roleTreeList();

    /**
     * 获取角色列表树
     *
     * @return
     * @date 2017年2月18日 上午10:32:04
     */
    List<ZTreeNode> roleTreeListByRoleId(Integer[] roleId);

    /**
     * 根据用户id获取角色列表
     * @param userId 用户id
     * @return
     */
    List<Role> listByUserId(Integer userId);

}
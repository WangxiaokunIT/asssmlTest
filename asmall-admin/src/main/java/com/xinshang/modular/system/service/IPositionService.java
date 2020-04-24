package com.xinshang.modular.system.service;

import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.system.model.Position;
import com.baomidou.mybatisplus.service.IService;
import com.xinshang.modular.system.model.User;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 职位表 服务类
 * </p>
 *
 * @author zhangjiajia
 * @since 2018-11-06
 */
public interface IPositionService extends IService<Position> {


    /**
     * 根据部门id查询其下职位树
     * @param deptId
     * @return
     */
    List<ZTreeNode> listTree(Integer deptId);


    /**
     * 根据职位id查询用户
     * @param positionId
     * @return
     */
    List<User> getUserByPositonId(Integer positionId);

    /**
     * 查询该职位的所在部门下未加入到该职位的用户
     * @param positionId
     * @return
     */
    List<User> getUserByDeptIdExceptByPositionId(Integer positionId);

    /**
     * 查询其下的职位
     * @param parentId
     * @return
     */
    List<ZTreeNode> syncPositionTree(Integer parentId);
}

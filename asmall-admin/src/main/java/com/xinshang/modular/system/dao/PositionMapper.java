package com.xinshang.modular.system.dao;

import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.system.model.Position;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xinshang.modular.system.model.User;
import java.util.List;

/**
 * <p>
 * 职位表 Mapper 接口
 * </p>
 *
 * @author zhangjiajia
 * @since 2018-11-06
 */
public interface PositionMapper extends BaseMapper<Position> {


    /**
     * 查询该部门下的职位树
     * @param deptId
     * @return
     */
    List<ZTreeNode> listTree(Integer deptId);

    /**
     * 查询该职位的所在部门下未加入到该职位的用户
     * @param positionId
     * @return
     */
    List<User> getUserByDeptIdExceptByPositionId(Integer positionId);

    /**
     * 根据职位id查询用户
     * @param positionId
     * @return
     */
    List<User> getUserByPositonId(Integer positionId);

    /**
     * 根据用户id获取职位集合
     * @param userId
     * @return
     */
    List<Position> listByUserId(Integer userId);

    /**
     * 异步获取职位
     * @param parentId
     * @return
     */
    List<ZTreeNode> syncPositionTree(Integer parentId);
}

package com.xinshang.modular.system.service.impl;

import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.system.model.Position;
import com.xinshang.modular.system.dao.PositionMapper;
import com.xinshang.modular.system.model.User;
import com.xinshang.modular.system.service.IPositionService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * <p>
 * 职位表 服务实现类
 * </p>
 *
 * @author zhangjiajia
 * @since 2018-11-06
 */
@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements IPositionService {


    /**
     * 获取部门下的职位树
     * @param deptId
     * @return
     */
    @Override
    public List<ZTreeNode> listTree(Integer deptId) {
        return this.baseMapper.listTree(deptId);
    }



    /**
     * 根据职位id查询用户
     * @param positionId
     * @return
     */
    @Override
    public List<User> getUserByPositonId(Integer positionId) {
        return this.baseMapper.getUserByPositonId(positionId);
    }

    /**
     * 查询该职位的所在部门下未加入到该职位的用户
     * @param positionId
     * @return
     */
    @Override
    public List<User> getUserByDeptIdExceptByPositionId(Integer positionId) {
        return this.baseMapper.getUserByDeptIdExceptByPositionId(positionId);
    }

    /**
     * 根据上级id查询其下的职位
     * @param parentId
     * @return
     */
    @Override
    public List<ZTreeNode> syncPositionTree(Integer parentId) {
        return this.baseMapper.syncPositionTree(parentId);
    }
}

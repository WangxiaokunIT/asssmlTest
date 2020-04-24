package com.xinshang.modular.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.system.model.Dept;
import com.xinshang.core.node.ZTreeNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author zhangjiajia
 * @since 2017-07-11
 */
public interface DeptMapper extends BaseMapper<Dept> {

    /**
     * 获取部门树
     * @return
     */
    List<ZTreeNode> listTree();


    /**
     * 异步获取ztree的节点查询部门及岗位
     * @param parentId
     * @return
     */
    List<ZTreeNode> syncDeptAndPositionTree(Integer parentId);

}
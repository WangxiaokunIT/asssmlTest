package com.xinshang.modular.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.system.model.Dept;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.system.model.Dept;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 部门服务
 *
 * @author fengshuonan
 * @date 2017-04-27 17:00
 */
public interface IDeptService extends IService<Dept> {

    /**
     * 删除部门
     * @param deptId
     */
    void deleteDept(Integer deptId);

    /**
     * 获取ztree的节点列表
     * @return
     */
    List<ZTreeNode> listTree();

    /**
     * 获取该父类的下一级的部门及岗位
     * @param parentId
     * @return
     */
    List<ZTreeNode> syncDeptAndPositionTree(Integer parentId);

}

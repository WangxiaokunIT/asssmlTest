package com.xinshang.modular.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.system.dao.DeptMapper;
import com.xinshang.modular.system.model.Dept;
import com.xinshang.modular.system.service.IDeptService;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.system.dao.DeptMapper;
import com.xinshang.modular.system.model.Dept;
import com.xinshang.modular.system.service.IDeptService;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.system.dao.DeptMapper;
import com.xinshang.modular.system.model.Dept;
import com.xinshang.modular.system.service.IDeptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author zhangjiajia
 * @date 2018年11月20日 12:33:07
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

    @Resource
    private DeptMapper deptMapper;

    @Override
    public void deleteDept(Integer deptId) {
        Dept dept = deptMapper.selectById(deptId);
        Wrapper<Dept> wrapper = new EntityWrapper<>();
        wrapper = wrapper.like("seq",  dept.getSeq());
        deptMapper.delete(wrapper);
    }

    @Override
    public List<ZTreeNode> listTree() {
        return this.baseMapper.listTree();
    }

    @Override
    public List<ZTreeNode> syncDeptAndPositionTree(Integer parentId) {
        return this.baseMapper.syncDeptAndPositionTree(parentId);
    }


}

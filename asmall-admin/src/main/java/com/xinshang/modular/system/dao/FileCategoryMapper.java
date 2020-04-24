package com.xinshang.modular.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.system.model.FileCategory;

import java.util.List;

/**
 * <p>
 * 文件类别 Mapper 接口
 * </p>
 *
 * @author zhangjiajia123
 * @since 2018-07-03
 */
public interface FileCategoryMapper extends BaseMapper<FileCategory> {

    /**
     * 获取ztree的节点列表
     */
    List<ZTreeNode> tree();

    /**
     * 插入
     */
    Integer insertSelective(FileCategory entity);

    FileCategory getById(Integer id);

    List<FileCategory> queryList(String condition);
}

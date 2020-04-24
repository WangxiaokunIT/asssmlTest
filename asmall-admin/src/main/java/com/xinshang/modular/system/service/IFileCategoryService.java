package com.xinshang.modular.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.system.model.FileCategory;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.system.model.FileCategory;

import java.util.List;

/**
 * <p>
 * 文件类别 服务类
 * </p>
 *
 * @author zhangjiajia123
 * @since 2018-07-03
 */
public interface IFileCategoryService extends IService<FileCategory> {

    /**
     * 获取ztree的节点列表
     */
    List<ZTreeNode> tree();

    /**
     * 根据id查询
     */
    FileCategory getById(Integer id);

    List<FileCategory> queryList(String condition);
}

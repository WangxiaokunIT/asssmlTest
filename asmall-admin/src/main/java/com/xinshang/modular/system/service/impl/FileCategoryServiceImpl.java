package com.xinshang.modular.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.system.dao.FileCategoryMapper;
import com.xinshang.modular.system.model.FileCategory;
import com.xinshang.modular.system.service.IFileCategoryService;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.system.dao.FileCategoryMapper;
import com.xinshang.modular.system.model.FileCategory;
import com.xinshang.modular.system.service.IFileCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 文件类别 服务实现类
 * </p>
 *
 * @author zhangjiajia123
 * @since 2018-07-03
 */
@Service
public class FileCategoryServiceImpl extends ServiceImpl<FileCategoryMapper, FileCategory> implements IFileCategoryService {

    @Override
    public List<ZTreeNode> tree() {
        return this.baseMapper.tree();
    }


    @Override
    public FileCategory getById(Integer id) {
        return baseMapper.getById(id);
    }

    @Override
    public List<FileCategory> queryList(String condition) {
        return baseMapper.queryList(condition);
    }


}

package com.xinshang.rest.modular.asmall.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.rest.modular.asmall.dao.ItemCategoryMapper;
import com.xinshang.rest.modular.asmall.model.Category;
import com.xinshang.rest.modular.asmall.model.ItemCategory;
import com.xinshang.rest.modular.asmall.service.IItemCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品分类 服务实现类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-11-25
 */
@Service
public class ItemCategoryServiceImpl extends ServiceImpl<ItemCategoryMapper, ItemCategory> implements IItemCategoryService {

    /**
     * 加载分类树
     * @return
     */
    @Override
    public List<Category> loadTree() {
        return this.baseMapper.loadTree();
    }
}

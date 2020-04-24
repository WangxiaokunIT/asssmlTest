package com.xinshang.modular.biz.service.impl;

import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.biz.model.ItemCategory;
import com.xinshang.modular.biz.dao.ItemCategoryMapper;
import com.xinshang.modular.biz.service.IItemCategoryService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
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
    public List<ZTreeNode> loadTree() {
        return this.baseMapper.loadTree();
    }
}

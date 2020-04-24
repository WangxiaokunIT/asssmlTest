package com.xinshang.rest.modular.asmall.service;

import com.baomidou.mybatisplus.service.IService;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.rest.modular.asmall.model.Category;
import com.xinshang.rest.modular.asmall.model.ItemCategory;

import java.util.List;

/**
 * <p>
 * 商品分类 服务类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-11-25
 */
public interface IItemCategoryService extends IService<ItemCategory> {

    /**
     * 加载分类树
     * @return
     */
    List<Category> loadTree();
}

package com.xinshang.modular.biz.service;

import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.biz.model.ItemCategory;
import com.baomidou.mybatisplus.service.IService;

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
    List<ZTreeNode> loadTree();
}

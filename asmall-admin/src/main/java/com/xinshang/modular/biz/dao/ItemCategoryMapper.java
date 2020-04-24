package com.xinshang.modular.biz.dao;

import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.biz.model.ItemCategory;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 商品分类 Mapper 接口
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-11-25
 */
public interface ItemCategoryMapper extends BaseMapper<ItemCategory> {

    /**
     * 加载分类树
     * @return
     */
    List<ZTreeNode> loadTree();
}

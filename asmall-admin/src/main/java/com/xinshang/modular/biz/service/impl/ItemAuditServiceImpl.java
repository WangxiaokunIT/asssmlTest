package com.xinshang.modular.biz.service.impl;

import com.xinshang.modular.biz.model.ItemAudit;
import com.xinshang.modular.biz.dao.ItemAuditMapper;
import com.xinshang.modular.biz.service.IItemAuditService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 商品发布审核表 服务实现类
 * </p>
 *
 * @author sunhao
 * @since 2019-10-18
 */
@Service
public class ItemAuditServiceImpl extends ServiceImpl<ItemAuditMapper, ItemAudit> implements IItemAuditService {


    @Autowired
    private ItemAuditMapper itemAuditMapper;
    /**
     * 根据商品ID查询商品审核详情
     *
     * @param itemId
     * @return
     */
    @Override
    public ItemAudit selectByProductId(Long itemId) {


        return itemAuditMapper.selectByProductId(itemId);
    }
}

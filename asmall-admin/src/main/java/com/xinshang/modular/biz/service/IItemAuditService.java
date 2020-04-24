package com.xinshang.modular.biz.service;

import com.xinshang.modular.biz.model.ItemAudit;
import com.baomidou.mybatisplus.service.IService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品发布审核表 服务类
 * </p>
 *
 * @author sunhao
 * @since 2019-10-18
 */
public interface IItemAuditService extends IService<ItemAudit> {

    /**
     * 根据商品ID查询商品审核详情
     * @param itemId
     * @return
     */
     ItemAudit selectByProductId(Long itemId);

}

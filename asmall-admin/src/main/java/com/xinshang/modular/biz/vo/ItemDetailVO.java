package com.xinshang.modular.biz.vo;

import com.xinshang.modular.biz.model.Item;
import lombok.Data;

/**
 * 商品详情
 * @author sunhao
 */
@Data
public class ItemDetailVO extends Item {

    /**
     * 要显示的商品信息
     */
    private String tittle;
}

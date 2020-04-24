package com.xinshang.modular.biz.vo;

import com.xinshang.modular.biz.model.Supplier;
import lombok.Data;

/**
 * 检索查询
 * @author lyk
 */
@Data
public class SearchVO extends Supplier {
    /**
     * 检索条件
     */
    private String searchContent;
}

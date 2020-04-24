package com.xinshang.modular.biz.vo;

import com.xinshang.modular.biz.model.CashOut;
import lombok.Data;

/**
 * 项目增强显示对象
 * @author lyk
 */
@Data
public class CashOutVO extends CashOut {
    /**
     * 创建人姓名
     */
    private String createUserName;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 银行卡号
     */
    private String bankCardName;
}

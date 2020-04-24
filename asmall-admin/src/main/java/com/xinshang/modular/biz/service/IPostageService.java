package com.xinshang.modular.biz.service;

import com.xinshang.modular.biz.model.Postage;
import com.baomidou.mybatisplus.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangxiaokun
 * @since 2019-12-09
 */
public interface IPostageService extends IService<Postage> {

    /**
     * 根据产品编码和省查询运费
     *
     */
    BigDecimal getFreight(String itemNumber,String province);

}

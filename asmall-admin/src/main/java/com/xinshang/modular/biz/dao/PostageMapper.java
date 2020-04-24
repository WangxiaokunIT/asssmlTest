package com.xinshang.modular.biz.dao;

import com.xinshang.modular.biz.model.Postage;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangxiaokun
 * @since 2019-12-09
 */
public interface PostageMapper extends BaseMapper<Postage> {

    /**
     * 根据产品编码和省获取运费
     * @param itemNumber
     * @param province
     * @return
     */
    BigDecimal getFreight(@Param("itemNumber") String itemNumber, @Param("province") String province);
}

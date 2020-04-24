package com.xinshang.modular.biz.service.impl;

import com.xinshang.modular.biz.model.Postage;
import com.xinshang.modular.biz.dao.PostageMapper;
import com.xinshang.modular.biz.service.IPostageService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangxiaokun
 * @since 2019-12-09
 */
@Service
@AllArgsConstructor
public class PostageServiceImpl extends ServiceImpl<PostageMapper, Postage> implements IPostageService {

    private final PostageMapper postageMapper;


    /**
     * 根据产品编码和省获取运费
     * @param itemNumber
     * @param province
     * @return
     */
    @Override
    public BigDecimal getFreight(String itemNumber, String province) {
       return postageMapper.getFreight(itemNumber,province);

    }
}

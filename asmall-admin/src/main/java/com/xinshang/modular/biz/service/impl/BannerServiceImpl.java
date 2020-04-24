package com.xinshang.modular.biz.service.impl;

import com.xinshang.modular.biz.model.Banner;
import com.xinshang.modular.biz.dao.BannerMapper;
import com.xinshang.modular.biz.service.IBannerService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.modular.biz.vo.ItemDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页轮播图 服务实现类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-11-29
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements IBannerService {

    @Autowired
    private BannerMapper bannerMapper;
    /**
     * 图片关联商品的详情list
     * @param tittle
     * @return
     */
    @Override
    public List<ItemDetailVO> searchItem(String tittle) {

        return bannerMapper.searchItem(tittle);
    }
}

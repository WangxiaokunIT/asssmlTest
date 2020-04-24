package com.xinshang.rest.modular.asmall.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.rest.modular.asmall.dao.BannerMapper;
import com.xinshang.rest.modular.asmall.model.Banner;
import com.xinshang.rest.modular.asmall.service.IBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
    private  BannerMapper bannerMapper;
}

package com.xinshang.modular.biz.service;

import com.xinshang.modular.biz.model.Banner;
import com.baomidou.mybatisplus.service.IService;
import com.xinshang.modular.biz.vo.ItemDetailVO;
import com.xinshang.modular.biz.vo.ItemVO;

import java.util.List;

/**
 * <p>
 * 首页轮播图 服务类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-11-29
 */
public interface IBannerService extends IService<Banner> {

    /**
     * 图片关联商品的详情list
     * @param tittle
     * @return
     */
    List<ItemDetailVO> searchItem(String tittle);
}

package com.xinshang.modular.biz.dao;

import com.xinshang.modular.biz.model.Banner;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xinshang.modular.biz.vo.ItemDetailVO;

import java.util.List;

/**
 * <p>
 * 首页轮播图 Mapper 接口
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-11-29
 */
public interface BannerMapper extends BaseMapper<Banner> {

   List<ItemDetailVO> searchItem(String tittle);
}

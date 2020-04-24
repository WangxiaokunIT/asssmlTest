package com.xinshang.rest.modular.asmall.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.modular.asmall.model.Banner;
import com.xinshang.rest.modular.asmall.service.IBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @title:首页轮播图控制器
 *
 * @author: sunhao
 * @since: 2019-11-29 09:17:53
 */
@RequestMapping("/banner")
@RestController
@Api(value = "首页轮播图",tags = "首页轮播图接口")
@AllArgsConstructor
public class BannerController{

    private final IBannerService itemService;
    @ApiOperation(value = "获取首页轮播图")
    @PostMapping("showBanner")
    public R showBanner() {
        EntityWrapper<Banner> wrapper = new EntityWrapper<>();
        //获取所有显示的banner图
        List<Banner> list = itemService.selectList(wrapper.eq("state", 1));
        return R.ok(list);
    }
}

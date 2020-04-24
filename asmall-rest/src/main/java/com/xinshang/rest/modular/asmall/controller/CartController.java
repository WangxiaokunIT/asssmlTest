package com.xinshang.rest.modular.asmall.controller;


import com.xinshang.rest.common.util.JwtTokenUtil;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.modular.asmall.model.Cart;
import com.xinshang.rest.modular.asmall.model.Member;
import com.xinshang.rest.modular.asmall.service.ICartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 购物车信息
 * @author wangxiaokun
 */
@RestController
@RequestMapping("/cart")
@Api(value = "购物车信息",tags = "购物车相关接口")
public class CartController {

    @Autowired
    private ICartService cartService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @ApiOperation(value = "添加购物车", notes = "添加购物车")
    @PostMapping("/addCart")
    public R addCart(@RequestBody Cart cart, HttpServletRequest request) {
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if (member == null) {
            return R.failed("用户不存在");
        }
        cart.setMemberId(member.getId());
        R r = cartService.addCart(cart);
        return r;
    }

    @ApiOperation(value = "删除购物车", notes = "删除购物车")
    @PostMapping("/delCart")
    public R delCart(@RequestBody Cart cart, HttpServletRequest request) {
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if (member == null) {
            return R.failed("用户不存在");
        }
        cart.setMemberId(member.getId());
        R r = cartService.delCart(cart);
        return r;
    }

    @ApiOperation(value = "获取购物车商品列表", notes = "获取购物车商品列表")
    @PostMapping("/cartList")
    public R cartList(HttpServletRequest request) {
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if (member == null) {
            return R.failed("用户不存在");
        }
        R r = cartService.getCartList(member.getId());
        return r;
    }

    @ApiOperation(value = "编辑购物车商品", notes = "编辑购物车商品")
    @PostMapping("/editCart")
    public R editCart(@RequestBody Cart cart, HttpServletRequest request) {
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if (member == null) {
            return R.failed("用户不存在");
        }
        cart.setMemberId(member.getId());
        R r = cartService.editCart(cart);
        return r;
    }





}

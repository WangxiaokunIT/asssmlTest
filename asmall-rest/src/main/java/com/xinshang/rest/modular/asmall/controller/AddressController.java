package com.xinshang.rest.modular.asmall.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xinshang.rest.common.util.JwtTokenUtil;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.modular.asmall.model.Address;

import com.xinshang.rest.modular.asmall.model.Dict;
import com.xinshang.rest.modular.asmall.model.Member;
import com.xinshang.rest.modular.asmall.service.IAddressService;
import com.xinshang.rest.modular.asmall.service.IDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 地址信息
 *
 * @author wangxiaokun
 */
@RestController
@RequestMapping("/address")
@Api(value = "地址信息",tags = "收货地址相关接口")
public class AddressController {

    @Autowired
    private IAddressService addressService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private IDictService dictService;

    @ApiOperation(value = "获取物流公司列表信息", notes = "获取物流公司列表信息")
    @PostMapping("/getLogistics")
    public R getLogistics(HttpServletRequest request) {
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if (member == null) {
            return R.failed("用户不存在");
        }
        List<Dict> dicts = dictService.selectList(new EntityWrapper<Dict>().eq("parent_id", 539));
        return R.ok(dicts);
    }

    @ApiOperation(value = "创建或修改地址", notes = "创建或修改地址")
    @PostMapping("/createOrUpdateAddress")
    public R createOrUpdateAddress(@RequestBody Address address, HttpServletRequest request) {
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if (member == null) {
            return R.failed("用户不存在");
        }
        address.setUserId(member.getId());
        R r = addressService.createOrUpdateAddress(address);
        return r;
    }

    @ApiOperation(value = "删除地址", notes = "删除地址")
    @PostMapping("/delAddress")
    public R delAddress(@RequestBody Address address, HttpServletRequest request) {
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if (member == null) {
            return R.failed("用户不存在");
        }
        boolean b = addressService.deleteById(address.getAddressId());
        if (b) {
            return R.ok("删除成功");
        } else {
            return R.failed("删除失败");
        }
    }

    @ApiOperation(value = "获取单个地址", notes = "获取单个地址")
    @PostMapping("/getAddressById")
    public R getAddressById(@RequestBody Address address, HttpServletRequest request) {
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if (member == null) {
            return R.failed("用户不存在");
        }
        Address addressOne = addressService.selectById(address.getAddressId());
        return R.ok(addressOne);
    }

    @ApiOperation(value = "获取地址列表", notes = "获取地址列表")
    @PostMapping("/getAddressList")
    public R getAddressList(HttpServletRequest request) {
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if (member == null) {
            return R.failed("用户不存在");
        }
        EntityWrapper<Address> addressWrapper = new EntityWrapper<>();
        addressWrapper.eq("user_id", member.getId());
        List<Address> list = addressService.selectList(addressWrapper);
        return R.ok(list);
    }
}

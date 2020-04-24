package com.xinshang.rest.modular.asmall.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.modular.asmall.dao.AddressMapper;
import com.xinshang.rest.modular.asmall.model.Address;
import com.xinshang.rest.modular.asmall.service.IAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 收货地址 服务实现类
 * </p>
 *
 * @author wangxiaokun
 * @since 2019-10-27
 */
@Service
@Slf4j
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public R createOrUpdateAddress(Address address) {

        //首先验证是否是默认地址
        Integer isDefault = address.getIsDefault();
        if (isDefault == 1) {
            EntityWrapper<Address> addressWrapper = new EntityWrapper<>();
            addressWrapper.eq("user_id", address.getUserId());
            addressWrapper.eq("is_default", 1);
            //只会有一个默认地址，对数据库已经设置默认地址状态进行修改
            Address tempAddress = address.selectOne(addressWrapper);
            if (null != tempAddress) {
                tempAddress.setIsDefault(0);
                addressMapper.updateById(tempAddress);
            }
        }
        //判断addressId   如果没有则添加 如果有则更新
        if (!Optional.ofNullable(address.getAddressId()).isPresent() == true) {
            log.info(!Optional.ofNullable(address.getAddressId()).isPresent() + "--------------");
            //默认设置 如果此地址为默认，则取消其他默认地址设定
            Integer insert = addressMapper.insert(address);
            if (insert == 1) {
                return R.ok("新增地址成功");
            } else {
                return R.ok("新增地址失败");
            }
        } else {
            Integer flag = addressMapper.updateById(address);
            if (flag == 1) {
                return R.ok("修改地址成功");
            } else {
                return R.ok("修改地址失败");
            }
        }
    }
}

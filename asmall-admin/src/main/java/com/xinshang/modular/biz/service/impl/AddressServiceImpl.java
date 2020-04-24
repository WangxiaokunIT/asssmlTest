package com.xinshang.modular.biz.service.impl;

import com.xinshang.modular.biz.model.Address;
import com.xinshang.modular.biz.dao.AddressMapper;
import com.xinshang.modular.biz.service.IAddressService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 收货地址 服务实现类
 * </p>
 *
 * @author wangxiaokun
 * @since 2019-10-27
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

}

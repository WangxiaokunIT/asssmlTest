package com.xinshang.rest.modular.asmall.service;

import com.baomidou.mybatisplus.service.IService;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.modular.asmall.dto.AddressDTO;
import com.xinshang.rest.modular.asmall.model.Address;

/**
 * <p>
 * 收货地址 服务类
 * </p>
 *
 * @author wangxiaokun
 * @since 2019-10-27
 */
public interface IAddressService extends IService<Address> {

    R createOrUpdateAddress(Address address);

}

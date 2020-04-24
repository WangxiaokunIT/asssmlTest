package com.xinshang.rest.modular.asmall.service;


import com.xinshang.core.enums.UserTypeEnum;

/**
 * @author zhangjiajia
 */
public interface IUserService {

    /**
     * 根据编码判断是客户还是供应商
     * @param code
     * @return
     */
    UserTypeEnum checkMemberOrSupplierByCode (String code);

}

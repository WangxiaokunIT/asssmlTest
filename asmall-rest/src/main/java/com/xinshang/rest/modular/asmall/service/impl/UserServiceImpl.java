package com.xinshang.rest.modular.asmall.service.impl;

import com.xinshang.core.enums.UserTypeEnum;
import com.xinshang.core.util.NoUtil;
import com.xinshang.rest.modular.asmall.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * @author zhangjiajia
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    /**
     * 根据编码判断是客户还是供应商
     * @param code
     * @return
     */
    @Override
    public UserTypeEnum checkMemberOrSupplierByCode(String code) {
        String decryptCode = NoUtil.decryptCode(code);
        String[] split = decryptCode.split("|");
        return UserTypeEnum.valueOf(Optional.ofNullable(split).filter(s->s.length>2).map(s->s[1]).orElse("1"));
    }


}

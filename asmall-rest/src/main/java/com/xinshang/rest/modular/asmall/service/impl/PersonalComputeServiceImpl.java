package com.xinshang.rest.modular.asmall.service.impl;

import com.xinshang.rest.modular.asmall.dao.PersonalComputeMapper;
import com.xinshang.rest.modular.asmall.service.PersonalComputeService;
import com.xinshang.rest.modular.asmall.vo.PCMemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lyk
 */
@Service
public class PersonalComputeServiceImpl implements PersonalComputeService {

    @Autowired
    private PersonalComputeMapper personalComputeMapper;

    @Override
    public PCMemberVO showMember(Long id) {
        return personalComputeMapper.showMember(id);
    }
}

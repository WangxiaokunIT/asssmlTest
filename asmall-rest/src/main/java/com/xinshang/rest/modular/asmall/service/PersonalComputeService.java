package com.xinshang.rest.modular.asmall.service;

import com.xinshang.rest.modular.asmall.vo.PCMemberVO;

/**
 * PC端使用
 * @author  lyk
 */
public interface PersonalComputeService {

    /**
     * 根据用户id获取用户信息
     * @param id
     * @return
     */
    PCMemberVO showMember(Long id);

}

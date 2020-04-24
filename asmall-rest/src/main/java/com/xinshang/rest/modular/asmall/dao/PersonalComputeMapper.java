package com.xinshang.rest.modular.asmall.dao;


import com.xinshang.rest.modular.asmall.vo.PCMemberVO;
import org.springframework.stereotype.Repository;

/**
 * @author lyk
 */
@Repository
public interface PersonalComputeMapper {

    /**
     * 根据用户id获取用户信息
     * @param id
     * @return
     */
    PCMemberVO showMember(Long id);
}

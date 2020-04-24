package com.xinshang.rest.modular.asmall.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xinshang.rest.modular.asmall.dto.MemberDTO;
import com.xinshang.rest.modular.asmall.model.Member;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-10-19
 */
public interface MemberDTOMapper extends BaseMapper<MemberDTO> {
    /**
     * 根据ID查询用户信息
     * @param id
     * @return
     */
    MemberDTO selectDetailbyId(Long id);
}

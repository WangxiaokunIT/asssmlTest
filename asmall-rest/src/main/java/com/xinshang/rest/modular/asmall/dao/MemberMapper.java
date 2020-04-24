package com.xinshang.rest.modular.asmall.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xinshang.rest.modular.asmall.model.Member;
import com.xinshang.rest.modular.asmall.vo.BankVO;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-10-19
 */
@Repository
public interface MemberMapper extends BaseMapper<Member> {
       /**
            * 功能描述: 根据ID查询用户信息
            * @param: id
            * @return:
            * @author: sunhao
            * @date: 2019/12/18 17:45
            */
       BankVO selectDetailbyId(Long id);
}

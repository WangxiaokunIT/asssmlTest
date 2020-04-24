package com.xinshang.modular.biz.service;

import com.xinshang.modular.biz.model.Account;
import com.xinshang.modular.biz.model.Member;
import com.baomidou.mybatisplus.service.IService;
import com.xinshang.modular.biz.vo.AccountVO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-10-19
 */
public interface IMemberService extends IService<Member> {
    /**
     * 更改会员状态
     * @param username
     * @param state
     * @return
     */
    Boolean alertMemberState(String username,Integer state);
    /**
     * 更改会员审核状态
     * @param id
     * @param state,vipState
     * @return
     */
    Boolean alertMemberVipState(Long  id,Integer state,Integer vipState);

    /**
     * 获取客户通联账户余额
     * @param bizUserId
     * @return
     */
    Account getAccount(String bizUserId);

    /**
     * 老客户激活
     * @param member
     */
    void active(Member member);
}

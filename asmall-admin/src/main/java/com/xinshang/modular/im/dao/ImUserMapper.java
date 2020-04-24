package com.xinshang.modular.im.dao;

import com.xinshang.modular.im.model.ImUser;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 用户 Mapper 接口
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-07-05
 */
public interface ImUserMapper extends BaseMapper<ImUser> {

    /**
     * 根据用户id查询其所有的好友
     * @param userId
     * @return
     */
    List<Integer> findFriendByUserId(Integer userId);

}

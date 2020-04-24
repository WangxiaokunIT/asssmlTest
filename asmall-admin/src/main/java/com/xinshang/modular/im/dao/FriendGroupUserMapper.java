package com.xinshang.modular.im.dao;

import com.xinshang.modular.im.dto.InitFriendGroup;
import com.xinshang.modular.im.model.FriendGroupUser;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 分组好友 Mapper 接口
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-07-05
 */
public interface FriendGroupUserMapper extends BaseMapper<FriendGroupUser> {

    /**
     * 根据用户id查询其好友列表
     * @param userId
     * @return
     */
    List<InitFriendGroup> initFriendGroupByUserId(Integer userId);
}

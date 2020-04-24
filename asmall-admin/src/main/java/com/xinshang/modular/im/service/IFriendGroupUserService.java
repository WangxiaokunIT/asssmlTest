package com.xinshang.modular.im.service;

import com.xinshang.modular.im.dto.InitFriendGroup;
import com.xinshang.modular.im.model.FriendGroupUser;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 分组好友 服务类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-07-05
 */
public interface IFriendGroupUserService extends IService<FriendGroupUser> {

    /**
     * 根据用户id查询其好友列表
     * @param userId
     * @return
     */
    List<InitFriendGroup> initFriendGroupByUserId(Integer userId);
}

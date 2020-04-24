package com.xinshang.modular.im.service.impl;

import com.xinshang.modular.im.dto.InitFriendGroup;
import com.xinshang.modular.im.model.FriendGroupUser;
import com.xinshang.modular.im.dao.FriendGroupUserMapper;
import com.xinshang.modular.im.service.IFriendGroupUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 分组好友 服务实现类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-07-05
 */
@Service
public class FriendGroupUserServiceImpl extends ServiceImpl<FriendGroupUserMapper, FriendGroupUser> implements IFriendGroupUserService {


    @Autowired
    private FriendGroupUserMapper friendGroupUserMapper;
    /**
     * 根据用户id查询其好友列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<InitFriendGroup> initFriendGroupByUserId(Integer userId) {

        return friendGroupUserMapper.initFriendGroupByUserId(userId);
    }
}

package com.xinshang.modular.im.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xinshang.modular.im.dao.FriendGroupMapper;
import com.xinshang.modular.im.model.FriendGroup;
import com.xinshang.modular.im.model.FriendGroupUser;
import com.xinshang.modular.im.model.GroupUser;
import com.xinshang.modular.im.model.ImUser;
import com.xinshang.modular.im.dao.ImUserMapper;
import com.xinshang.modular.im.service.IImUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-07-05
 */
@Service
public class ImUserServiceImpl extends ServiceImpl<ImUserMapper, ImUser> implements IImUserService {
    @Autowired
    private FriendGroupMapper friendGroupMapper;

    @Autowired
    private ImUserMapper imUserMapper;

    /**
     * 添加用户
     * @param imUser
     * @return
     */
    @Override
    public Boolean addUser(ImUser imUser){
        //插入用户表
        imUser.insert();

        //给每个用户的我的好友里面添加此用户
        EntityWrapper<FriendGroup> ew = new EntityWrapper();
        ew.eq("is_system",1).eq("name","我的好友");
        FriendGroupUser fgu = new FriendGroupUser();
        fgu.setUserId(imUser.getId());
        friendGroupMapper.selectList(ew).forEach(fg->{
            fgu.setGroupId(fg.getId());
            fgu.insert();
        });

        //给此用户添加我的好友分组
        FriendGroup friendGroup = new FriendGroup();
        friendGroup.setUserId(imUser.getId());
        friendGroup.setIsSystem(1);
        friendGroup.setName("我的好友");
        friendGroup.insert();

        //给此用户的我的好友里面添加其他用户
        FriendGroupUser friendGroupUser = new FriendGroupUser();
        EntityWrapper<ImUser> entityWrapper = new EntityWrapper();
        entityWrapper.ne("id",imUser.getId());
        imUserMapper.selectList(entityWrapper).forEach(u->{
            friendGroupUser.setGroupId(friendGroup.getId());
            friendGroupUser.setUserId(u.getId());
            friendGroupUser.insert();
        });

        //添加此用户到大家一起聊群组
        GroupUser gu = new GroupUser();
        gu.setUserId(imUser.getId());
        gu.setGroupId(1);
        gu.insert();
        return true;
    }

}

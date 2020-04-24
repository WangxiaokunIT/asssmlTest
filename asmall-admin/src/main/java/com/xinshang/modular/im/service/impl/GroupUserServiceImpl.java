package com.xinshang.modular.im.service.impl;

import com.xinshang.modular.im.dto.InitUser;
import com.xinshang.modular.im.model.GroupUser;
import com.xinshang.modular.im.dao.GroupUserMapper;
import com.xinshang.modular.im.service.IGroupUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 群成员 服务实现类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-07-05
 */
@Service
public class GroupUserServiceImpl extends ServiceImpl<GroupUserMapper, GroupUser> implements IGroupUserService {


    /**
     * 获取群成员
     *
     * @param groupId
     * @return
     */
    @Override
    public List<InitUser> getMembersByGroupId(Integer groupId) {

        return this.baseMapper.getMembersByGroupId(groupId);
    }
}

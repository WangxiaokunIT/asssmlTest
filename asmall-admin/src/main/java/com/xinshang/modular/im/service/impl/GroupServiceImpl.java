package com.xinshang.modular.im.service.impl;

import com.xinshang.modular.im.dto.InitGroup;
import com.xinshang.modular.im.model.Group;
import com.xinshang.modular.im.dao.GroupMapper;
import com.xinshang.modular.im.service.IGroupService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 群组 服务实现类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-07-05
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements IGroupService {

    @Autowired
    private GroupMapper groupMapper;
    /**
     * 根据用户id查询初始化的群信息
     *
     * @param userId
     * @return
     */
    @Override
    public List<InitGroup> initGroupByUserId(Integer userId) {
        return groupMapper.initGroupByUserId(userId);
    }
}

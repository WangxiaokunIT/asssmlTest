package com.xinshang.modular.im.dao;

import com.xinshang.modular.im.dto.InitUser;
import com.xinshang.modular.im.model.GroupUser;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 群成员 Mapper 接口
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-07-05
 */
public interface GroupUserMapper extends BaseMapper<GroupUser> {


    /**
     * 获取群成员
     * @param groupId
     * @return
     */
    List<InitUser> getMembersByGroupId(Integer groupId);
}

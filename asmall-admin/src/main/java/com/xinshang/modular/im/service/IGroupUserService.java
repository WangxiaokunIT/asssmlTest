package com.xinshang.modular.im.service;

import com.xinshang.modular.im.dto.InitUser;
import com.xinshang.modular.im.model.GroupUser;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 群成员 服务类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-07-05
 */
public interface IGroupUserService extends IService<GroupUser> {

    /**
     * 获取群成员
     * @param groupId
     * @return
     */
    List<InitUser> getMembersByGroupId(Integer groupId);
}

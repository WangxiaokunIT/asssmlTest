package com.xinshang.modular.im.service;

import com.xinshang.modular.im.dto.InitGroup;
import com.xinshang.modular.im.model.Group;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 群组 服务类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-07-05
 */
public interface IGroupService extends IService<Group> {

    /**
     * 根据用户id查询初始化的群信息
     * @param userId
     * @return
     */
    List<InitGroup> initGroupByUserId(Integer userId);
}

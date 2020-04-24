package com.xinshang.modular.im.dao;

import com.xinshang.modular.im.dto.InitGroup;
import com.xinshang.modular.im.model.Group;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 群组 Mapper 接口
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-07-05
 */
public interface GroupMapper extends BaseMapper<Group> {

    /**
     * 根据用户id查询初始化的群信息
     * @param userId
     * @return
     */
    List<InitGroup> initGroupByUserId(Integer userId);

}

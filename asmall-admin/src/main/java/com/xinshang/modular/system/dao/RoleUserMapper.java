package com.xinshang.modular.system.dao;

import com.xinshang.modular.system.model.RoleUser;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 角色用户关联表 Mapper 接口
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-02-13
 */
public interface RoleUserMapper extends BaseMapper<RoleUser> {

    String getUserRoleName(Integer userId);
}

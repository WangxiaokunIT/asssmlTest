package com.xinshang.modular.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xinshang.core.datascope.DataScope;
import com.xinshang.modular.system.model.User;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * <p>
 * 管理员表 Mapper 接口
 * </p>
 *
 * @author zhangjiajia
 * @since 2017-07-11
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 修改用户状态
     */
    int setState(@Param("userId") Integer userId, @Param("state") int state);

    /**
     * 修改密码
     */
    int changePwd(@Param("userId") Integer userId, @Param("pwd") String pwd);

    /**
     * 根据条件查询用户列表
     */
    List<User> selectUsers(@Param("dataScope") DataScope dataScope, @Param("name") String name, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("deptId") Integer deptId, @Param("positionId") Integer positionId);

    /**
     * 通过账号获取用户
     * @param account
     */
    User getByAccount(@Param("account") String account);

    /**
     * 根据部门id查询其及子部门下的用户
     * @param deptId
     * @return
     */
    List<User> selectDeptUser(Integer deptId);

}
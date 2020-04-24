package com.xinshang.modular.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xinshang.core.datascope.DataScope;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.system.model.User;
import com.xinshang.core.datascope.DataScope;
import com.xinshang.modular.system.model.User;
import com.xinshang.core.datascope.DataScope;
import com.xinshang.modular.system.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author zhangjiajia
 * @since 2018-02-22
 */
public interface IUserService extends IService<User> {

    /**
     * 修改用户状态
     */
    int setState(Integer userId,int state);

    /**
     * 修改密码
     */
    int changePwd(Integer userId,String pwd);

    /**
     * 根据条件查询用户列表
     */
    List<User> selectUsers(DataScope dataScope,String name,String beginTime,String endTime,Integer deptId,Integer positionId);

    /**
     * 通过账号获取用户
     * @param account
     */
    User getByAccount(String account);

    /**
     * 根据部门id查询其部门及其子部门的用户
     * @param deptId
     * @return
     */
    List<User> selectDeptUser(Integer deptId);
}

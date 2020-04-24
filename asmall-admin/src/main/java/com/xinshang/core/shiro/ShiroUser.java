package com.xinshang.core.shiro;

import com.xinshang.modular.system.model.Position;
import com.xinshang.modular.system.model.Role;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息
 *
 * @author fengshuonan
 * @date 2016年12月5日 上午10:26:43
 */

@Data
public class ShiroUser implements Serializable {

    private static final long serialVersionUID = 1L;
    /**主键ID*/
    public Integer id;
    /**账号*/
    public String account;
    /**姓名*/
    public String name;
    /**全名*/
    public String fullName;
    /**部门id*/
    public Integer deptId;
    /**部门名称*/
    public String deptName;
    /**角色集合**/
    public List<Role> roles;
    /**岗位集合*/
    public List<Position> positions;


}

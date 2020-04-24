package com.xinshang.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xinshang.core.annotation.JoinField;

import java.io.Serializable;

/**
 * <p>
 * 角色用户关联表
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-02-13
 */
@TableName("sys_role_user")
public class RoleUser extends Model<RoleUser> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 角色id
     */
    @JoinField(tableName="sys_role",relationColumn="id",targetColumn="name")
    @TableField("role_id")
    private Integer roleId;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "RoleUser{" +
        "id=" + id +
        ", roleId=" + roleId +
        ", userId=" + userId +
        "}";
    }
}

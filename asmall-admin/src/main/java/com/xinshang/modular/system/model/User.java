package com.xinshang.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.xinshang.core.annotation.DictField;
import com.xinshang.core.annotation.JoinField;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author zhangjiajia
 * @since 2017-07-11
 */

@Data
@TableName("sys_user")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 头像
     */
	private String avatar;
    /**
     * 账号
     */
	private String account;
    /**
     * 密码
     */
	private String password;
    /**
     * md5密码盐
     */
	private String salt;
    /**
     * 名字
     */
	private String name;
	/**
	 * 全名
	 */
	@TableField("full_name")
	private String fullName;
    /**
     * 生日
     */
	private Date birthday;
    /**
     * 性别（1：男 2：女）
     */
	@DictField("sys_sex")
	private Integer sex;
    /**
     * 电子邮件
     */
	private String email;
    /**
     * 电话
     */
	private String phone;
    /**
     * 部门id
     */
	@TableField("dept_id")
	@JoinField(tableName="sys_dept",relationColumn="id",targetColumn="simple_name")
	private Integer deptId;
    /**
     * 状态(1：启用  2：冻结  3：删除）
     */
    @DictField("sys_state")
	private Integer state;
    /**
     * 保留字段
     */
	private Integer version;

	/**
	 * 拼音首字母
	 * @return
	 */
	@TableField("pin_yin_index")
	private String pinYinIndex;

	/**
	 * 职位描述
	 */
	private String remark;
	/**
	 * 创建人
	 */
	private Integer creator;
	/**
	 * 创建时间
	 */
	@TableField("gmt_create")
	private Date gmtCreate;
	/**
	 * 修改人
	 */
	private Integer modifier;
	/**
	 * 修改时间
	 */
	@TableField("gmt_modified")
	private Date gmtModified;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}

package com.xinshang.modular.system.transfer;

import com.baomidou.mybatisplus.activerecord.Model;
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
public class UserPosition extends Model<UserPosition> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	private Integer id;
    /**
     * 账号
     */
	private String account;
    /**
     * 名字
     */
	private String name;
	/**
	 * 全名
	 */
	private String fullName;
    /**
     * 生日
     */
	private Date birthday;
    /**
     * 性别（1：男 2：女）
     */
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
     * 角色id
     */
	private String roleId;
    /**
     * 部门id
     */
	private Integer deptId;
    /**
     * 状态(1：启用  2：冻结  3：删除）
     */
	private Integer state;
	/**
	 * 拼音首字母
	 * @return
	 */
	private String pinYinIndex;

	/**
	 * 描述
	 */
	private String remark;
	/**
	 * 创建人
	 */
	private Integer creator;
	/**
	 * 创建时间
	 */
	private Date gmtCreate;
	/**
	 * 修改人
	 */
	private Integer modifier;
	/**
	 * 修改时间
	 */
	private Date gmtModified;

	/**
	 * 职位编码
	 * @return
	 */
	private String positionCode;

	/**
	 * 职位名称
	 * @return
	 */
	private String positionName;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}

package com.xinshang.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.xinshang.core.annotation.JoinField;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author zhangjiajia
 * @since 2017-07-11
 */

@Data
@TableName("sys_role")
public class Role extends Model<Role> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 序号
     */
	@TableField("sort_num")
	private Integer sortNum;
    /**
     * 父角色id
     */
	@JoinField(tableName="sys_role",relationColumn="id",targetColumn="name")
	@TableField("parent_id")
	private Integer parentId;
    /**
     * 角色名称
     */
	private String name;

    /**
     * 提示
     */
	private String remark;
    /**
     * 保留字段(暂时没用）
     */
	private Integer version;

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

	/**
	 * 搜索码
	 */
	private String seq;

	/**
	 * 级别
	 */
	private Integer level;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}

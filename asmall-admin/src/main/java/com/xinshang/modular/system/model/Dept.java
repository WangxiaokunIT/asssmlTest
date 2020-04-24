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
 * 部门表
 * </p>
 *
 * @author zhangjiajia
 * @since 2017-07-11
 */
@Data
@TableName("sys_dept")
public class Dept extends Model<Dept> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 排序
     */
	@TableField("sort_num")
	private Integer sortNum;
    /**
     * 父部门id
     */
	@JoinField(tableName="sys_dept",relationColumn="id",targetColumn="simple_name")
	@TableField("parent_id")
	private Integer parentId;
    /**
     * 搜索码
     */
	private String seq;
    /**
     * 简称
     */
	@TableField("simple_name")
	private String simpleName;
    /**
     * 全称
     */
	@TableField("full_name")
	private String fullName;
    /**
     * 备注
     */
	private String remark;
	/**
	 * 级别
	 */
	private Integer level;
    /**
     * 版本（乐观锁保留字段）
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}

package com.xinshang.rest.modular.asmall.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author zhangjiajia
 * @since 2017-07-11
 */

@Data
@TableName("sys_dict")
public class Dict extends Model<Dict> {

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
	 * 父级字典
	 */
	@TableField("parent_id")
	private Integer parentId;
	/**
	 * 名称
	 */
	private String name;

	/**
	 * 编码
	 */
	private String code;
	/**
	 * 备注
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

package com.xinshang.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 通用表model
 * </p>
 *
 * @author zhangjiajia
 * @date 2019年1月10日 19:01:57
 */

@Data
public class GeneralTable extends Model<GeneralTable> {

    private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 表名字
	 */
	@TableId(value="table_name")
	private String tableName;

    /**
     * 连接字段
     */
	@TableId(value="relation_column")
	private String relationColumn;

	/**
	 * 获取的字段名
	 */
	@TableId(value="target_column")
	private String targetColumn;

	/**
	 * where字段名
	 */
	@TableId(value="where_column")
	private String whereColumn;

	/**
	 * where字段值
	 */
	@TableId(value="where_value")
	private String whereValue;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}

package com.xinshang.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xinshang.core.annotation.DictField;
import com.xinshang.core.annotation.JoinField;
import lombok.Data;
import java.io.Serializable;

/**
 * <p>
 * 职位表
 * </p>
 *
 * @author zhangjiajia
 * @since 2018-11-06
 */

@Data
@TableName("sys_position")
public class Position extends Model<Position> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 所属部门id
     */
    @TableField("dept_id")
    @JoinField(tableName="sys_dept",relationColumn="id",targetColumn="simple_name")
    private Integer deptId;
    /**
     * 上级职位id
     */
    @JoinField(tableName="sys_position",relationColumn="id",targetColumn="name")
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 职位编码
     */
    private String code;

    /**
     * 职位名称
     */
    private String name;
    /**
     * 级别
     */
    private Integer level;
    /**
     * 显示顺序
     */
    @TableField("sort_num")
    private Integer sortNum;
    /**
     * 搜索码
     */
    private String seq;
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

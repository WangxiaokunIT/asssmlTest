package com.xinshang.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 文件类别
 * </p>
 *
 * @author zhangjiajia123
 * @since 2018-07-03
 */

@Data
@TableName("sys_file_category")
public class FileCategory extends Model<FileCategory> {

    private static final long serialVersionUID = 1L;

    /**
     * 类别id
     */
    private Integer id;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 上级类别id
     */
    @TableField("parent_id")
    private Integer parentId;
    /**
     * 级别
     */
    private Integer level;
    /**
     * 搜索码
     */
    private String seq;
    /**
     * 显示顺序
     */
    @TableField("sort_num")
    private Integer sortNum;
    /**
     * 类别描述
     */
    private String remark;
    /**
     * 创建时间
     */
    @TableField("gmt_create")
    private Date gmtCreate;
    /**
     * 更新时间
     */
    @TableField("gmt_modified")
    private Date gmtModified;
    /**
     * 创建人
     */
    private Integer creator;
    /**
     * 修改人
     */
    private Integer modifier;
    /**
     * 上级类别名称
     */
    @TableField(exist = false)
    private String parentName;

    /**
     * 创建人名称
     */
    @TableField(exist = false)
    private String creatorName;

    /**
     * 修改人名称
     */
    @TableField(exist = false)
    private String modifierName;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

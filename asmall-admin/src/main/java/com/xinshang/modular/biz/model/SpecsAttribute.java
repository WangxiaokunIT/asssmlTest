package com.xinshang.modular.biz.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xinshang.core.annotation.JoinField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 规格属性名
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-11-25
 */
@TableName("tb_specs_attribute")
@Data
public class SpecsAttribute extends Model<SpecsAttribute> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 所属分类
     */
    @JoinField(tableName = "tb_item_category",relationColumn = "id",targetColumn = "name")
    @TableField("category_id")
    private Integer categoryId;

    /**
     * 属性名
     */
    @TableField("attribute_name")
    private String attributeName;

    /**
     * 属性值
     */
    @TableField("attribute_values")
    private String attributeValues;

    /**
     * 排序值
     */
    @TableField("sort_num")
    private Integer sortNum;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

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
 * 系统参数
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-11-26
 */
@TableName("tb_parameter")
@Data
public class Parameter extends Model<Parameter> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 参数名
     */
    @TableField("parm_name")
    private String parmName;
    /**
     * 参数值
     */
    @TableField("parm_value")
    private String parmValue;
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
    /**
     * 编码
     */
    private String code;

    /**
     * 备注
     */
    private String remark;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}

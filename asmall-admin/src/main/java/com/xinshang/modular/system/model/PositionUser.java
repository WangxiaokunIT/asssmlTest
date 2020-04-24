package com.xinshang.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import java.io.Serializable;

/**
 * <p>
 * 职位用户表
 * </p>
 *
 * @author zhangjiajia
 * @since 2018-11-12
 */

@Data
@TableName("sys_position_user")
public class PositionUser extends Model<PositionUser> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 职位id
     */
    @TableField("position_id")
    private Integer positionId;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

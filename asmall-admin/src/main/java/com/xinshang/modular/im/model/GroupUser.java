package com.xinshang.modular.im.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 群成员
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-07-05
 */

@Data
@TableName("im_group_user")
public class GroupUser extends Model<GroupUser> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 所属组
     */
    @TableField("group_id")
    private Integer groupId;
    /**
     * 用户
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 添加时间
     */
    @TableField("gmt_create")
    private Date gmtCreate;
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

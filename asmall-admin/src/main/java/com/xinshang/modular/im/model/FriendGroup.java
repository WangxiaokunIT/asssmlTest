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
 * 用户分组
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-07-05
 */

@Data
@TableName("im_friend_group")
public class FriendGroup extends Model<FriendGroup> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 名称
     */
    private String name;
    /**
     * 是否系统分组[0:否,1:是]
     */
    @TableField("is_system")
    private Integer isSystem;
    /**
     * 创建时间
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

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
 * 消息记录
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-07-05
 */

@Data
@TableName("im_msg_record")
public class MsgRecord extends Model<MsgRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 消息
     */
    @TableField("msg_content")
    private String msgContent;
    /**
     * 类型
     */
    private String type;
    /**
     * 发送者
     */
    @TableField("form_user")
    private Integer formUser;
    /**
     * 接收者
     */
    @TableField("to_user")
    private Integer toUser;

    /**
     * 消息json
     */
    @TableField("msg_json")
    private String msgJson;
    /**
     * 是否已读
     */
    @TableField("is_readed")
    private Integer isReaded;
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

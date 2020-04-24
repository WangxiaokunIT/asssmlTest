package com.xinshang.modular.biz.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * e签宝配置
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-10-24
 */
@TableName("tb_eqb_czkey")
public class EqbCzkey extends Model<EqbCzkey> {

    private static final long serialVersionUID = 1L;

    private String key;
    @TableField("scene_template_id")
    private String sceneTemplateId;
    @TableField("segment_templet_id")
    private String segmentTempletId;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSceneTemplateId() {
        return sceneTemplateId;
    }

    public void setSceneTemplateId(String sceneTemplateId) {
        this.sceneTemplateId = sceneTemplateId;
    }

    public String getSegmentTempletId() {
        return segmentTempletId;
    }

    public void setSegmentTempletId(String segmentTempletId) {
        this.segmentTempletId = segmentTempletId;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "EqbCzkey{" +
        "key=" + key +
        ", sceneTemplateId=" + sceneTemplateId +
        ", segmentTempletId=" + segmentTempletId +
        "}";
    }
}

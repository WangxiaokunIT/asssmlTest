package com.xinshang.modular.im.dto;

import lombok.Data;

/**
 * 接收消息对象
 * @author zhangjiajia
 * @since 19-7-5
 */
@Data
public class ReceiveMsgDTO {

    /**
     * 消息类型
     */
    private String type;
    /**
     * 消息数据
     */
    private ReceiveMsgData data;

}

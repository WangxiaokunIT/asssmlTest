package com.xinshang.modular.im.dto;


import lombok.Data;

/**
 * @author zhangjiajia
 * @since 19-7-5
 */
@Data
public class ReceiveMsgData {

    /**
     * 我的信息
     */
    private ReceiveMineInfo mine;
    /**
     * 对方信息
     */
    private ReceiveToInfo to;
}

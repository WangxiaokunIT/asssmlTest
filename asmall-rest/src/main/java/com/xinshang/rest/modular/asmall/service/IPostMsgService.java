package com.xinshang.rest.modular.asmall.service;


import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Service;

import javax.jms.Destination;

/**
 * @author sunha
 * @since 2019/8/411:35
 */
public interface IPostMsgService {
    /**
     * 向指定队列发送消息
     * @param msg
     */
    void sendMessage(final String msg);

    /**
     * 延迟投递
     * @param msg
     * @param scheduleMessagePostProcessor
     */
    void sendMessage(final String msg, MessagePostProcessor scheduleMessagePostProcessor);

}

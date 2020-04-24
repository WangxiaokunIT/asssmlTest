package com.xinshang.modular.system.service;


import org.springframework.jms.core.MessagePostProcessor;

import javax.jms.Destination;

/**
 * @author sunha
 * @since 2019/8/411:35
 */
public interface IPostMsgService {
    /**
     * 向指定队列发送消息
     * @param destination
     * @param msg
     */
    void sendMessage(Destination destination, final String msg);

    /**
     * 向默认队列发送消息
     * @param msg
     */
    void sendMessage(final String msg);

    /**
     * 延迟投递
     * @param msg
     * @param scheduleMessagePostProcessor
     */
    void sendMessage(Destination destination, Object msg, MessagePostProcessor scheduleMessagePostProcessor);


    /**
     * 延迟投递
     * @param msg
     * @param scheduleMessagePostProcessor
     */
    void sendMessage(Object msg, MessagePostProcessor scheduleMessagePostProcessor);

}

package com.xinshang.rest.modular.asmall.service.impl;

import com.xinshang.rest.modular.asmall.service.IPostMsgService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.Queue;

/**
 * @author sunha
 * @since 2019/8/319:22
 */
@Service
@AllArgsConstructor
@Slf4j
public class PostMsgServiceImpl implements IPostMsgService {

    private final JmsTemplate jmsTemplate;
    private final Queue messageQueue;

    /**
     * 向指定队列发送消息
     * @param msg
     */
    @Override
    public void sendMessage(final String msg) {
        log.info("向队列["+messageQueue.toString()+"]发送消息:"+msg);
        jmsTemplate.send(messageQueue,session-> session.createTextMessage(msg));
    }

    /**
     * 向指定队列发送延时消息
     * @param msg
     * @param messagePostProcessor
     */
    @Override
    public void sendMessage(String msg, MessagePostProcessor messagePostProcessor){
        log.info("向队列["+messageQueue.toString()+"]发送延时/定时消息:"+msg);
        jmsTemplate.convertAndSend(messageQueue,msg,messagePostProcessor);
    }
}

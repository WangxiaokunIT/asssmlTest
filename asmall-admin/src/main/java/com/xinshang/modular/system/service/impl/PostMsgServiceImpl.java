package com.xinshang.modular.system.service.impl;

import com.xinshang.modular.system.service.IPostMsgService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Service;

import javax.jms.Destination;

/**
 * @author sunha
 * @since 2019/8/319:22
 */
@Service
@AllArgsConstructor
@Slf4j
public class PostMsgServiceImpl implements IPostMsgService {

    private final JmsTemplate jmsTemplate;

    /**
     * 向指定队列发送消息
     * @param destination
     * @param msg
     */
    @Override
    public void sendMessage(Destination destination, final String msg) {
        log.info("向队列["+destination.toString()+"]发送消息:"+msg);
        jmsTemplate.send(destination,session-> session.createTextMessage(msg));
    }

    /**
     * 向默认队列发送消息
     * @param msg
     */
    @Override
    public void sendMessage(final String msg) {
        log.info("向队列["+jmsTemplate.getDefaultDestination().toString()+"]发送消息:"+msg);
        jmsTemplate.send(session-> session.createTextMessage(msg));

    }

    @Override
    public void sendMessage(Destination destination, Object msg, MessagePostProcessor messagePostProcessor){
        log.info("向队列["+destination.toString()+"]发送延时/定时消息:"+msg.toString());
        jmsTemplate.convertAndSend(destination,msg,messagePostProcessor);
    }

    @Override
    public void sendMessage(Object msg, MessagePostProcessor messagePostProcessor){
        log.info("向队列["+jmsTemplate.getDefaultDestination().toString()+"]发送延时/定时消息:"+msg);
        jmsTemplate.convertAndSend(msg,messagePostProcessor);
    }
}

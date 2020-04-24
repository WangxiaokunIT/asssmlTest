package com.xinshang.modular.system.controller;

import com.xinshang.core.activemq.ScheduleMessagePostProcessor;
import com.xinshang.modular.system.service.IPostMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.jms.Queue;

@RestController
public class JmsProducerController {

    @Autowired
    private IPostMsgService iPostMsgService;

    @Autowired
    private Queue messageQueue;

    @RequestMapping("/send")
    public Object send(){
        iPostMsgService.sendMessage(messageQueue,"spell:3", new ScheduleMessagePostProcessor(1L));
        iPostMsgService.sendMessage(messageQueue,"spell:4");
        return "发送完毕";
    }
}
package com.xinshang.rest.modular.asmall.controller;


import com.xinshang.rest.common.activemq.ScheduleMessagePostProcessor;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.modular.asmall.service.IPostMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;
/**
 * @author zhangjiajia
 */
@Api(value = "jms测试接口",tags = "jms测试接口")
@RestController
public class JmsProducerController {

    @Autowired
    private IPostMsgService iPostMsgService;

    @ApiOperation(value = "发送消息", notes = "发送消息")
    @GetMapping("/jms/send")
    public R send(){
        ScheduleMessagePostProcessor scheduleMessagePostProcessor = new ScheduleMessagePostProcessor(3000L);
        iPostMsgService.sendMessage("spell:3",scheduleMessagePostProcessor);
        iPostMsgService.sendMessage("spell:4");
        return R.ok("发送完毕");
    }
}
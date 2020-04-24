package com.xinshang.core.activemq;

/**
 * 延迟消息配置
 * @author sunha
 * @since 2019/8/319:45
 */

import lombok.Data;
import org.apache.activemq.ScheduledMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jms.core.MessagePostProcessor;

import javax.jms.JMSException;
import javax.jms.Message;

@Data
public class ScheduleMessagePostProcessor implements MessagePostProcessor {

    private long delay = 0L;

    private String corn = null;

    public ScheduleMessagePostProcessor(long delay) {
        this.delay = delay;
    }

    public ScheduleMessagePostProcessor(String cron) {
        this.corn = cron;
    }

    @Override
    public Message postProcessMessage(Message message) throws JMSException {
        if (delay > 0) {
            message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
        }
        if (!StringUtils.isEmpty(corn)) {
            message.setStringProperty(ScheduledMessage.AMQ_SCHEDULED_CRON, corn);
        }
        return message;
    }
}

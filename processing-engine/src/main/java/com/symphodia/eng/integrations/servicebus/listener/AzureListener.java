package com.symphodia.eng.integrations.servicebus.listener;

import com.symphodia.eng.integrations.servicebus.dto.Message;
import com.symphodia.eng.integrations.servicebus.dto.MessageBody;
import com.symphodia.eng.integrations.servicebus.dto.MessageType;
import com.symphodia.eng.integrations.servicebus.service.ErrorService;
import com.symphodia.eng.integrations.servicebus.service.MessageService;
import com.symphodia.spring.common.log.AutowiredLogger;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public abstract class AzureListener implements MessageListener {

    @AutowiredLogger
    protected Logger logger;

    @Autowired
    protected ErrorService errorService;

    @Autowired
    protected MessageService messageService;

    protected String getMessageContent(javax.jms.Message message) throws Exception {
        TextMessage textMessage = (TextMessage) message;
        return textMessage.getText();
    }

    public <T extends MessageBody> com.symphodia.eng.integrations.servicebus.dto.Message<T> convert(String json) {
        Message<T> orderMessage = messageService.convert(json);
        messageService.validate(orderMessage);
        return orderMessage;
    }

    protected void processError(String json, Exception ex, MessageType generatedMessageType) {
        logger.error("Error during processing json: " + json, ex);

        errorService.processException(json, ex, generatedMessageType);
    }

    protected void acknowledge(javax.jms.Message message) {
        try {
            message.acknowledge();
        } catch (JMSException e) {
            logger.error("Error during acknowledging message", e);
        }
    }
}

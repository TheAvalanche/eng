package com.symphodia.eng.integrations.servicebus.service;

import com.symphodia.spring.common.property.PropertyService;
import com.symphodia.eng.integrations.servicebus.dto.Message;
import com.symphodia.eng.integrations.servicebus.dto.event.MessageEvent;
import com.symphodia.spring.common.log.AutowiredLogger;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import static com.symphodia.eng.common.property.Property.AZURE_EVENT_TOPIC;


@Service
public class MessageProducer {

    @AutowiredLogger
    Logger logger;

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    PropertyService propertyService;

    @Autowired
    MessageService messageService;

    public void sendEvent(Message<? extends MessageEvent> message) {

        String json = messageService.convert(message);

        logger.debug("Sending message: {}", json);

        jmsTemplate.send(propertyService.getValue(AZURE_EVENT_TOPIC), new JsonMessageCreator(json));
    }



}

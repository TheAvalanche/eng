package com.symphodia.eng.integrations.servicebus.listener;

import com.symphodia.eng.core.service.EventProcessEngine;
import com.symphodia.eng.integrations.servicebus.dto.Message;
import com.symphodia.eng.integrations.servicebus.dto.MessageType;
import com.symphodia.eng.integrations.servicebus.dto.event.MessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventListener extends AzureListener {

    @Autowired
    EventProcessEngine eventProcessEngine;

    @Override
    public void onMessage(javax.jms.Message message) {
        String json = null;
        try {
            json = getMessageContent(message);

            logger.debug("Received message from event listener: {}", json);

            Message<MessageEvent> eventMessage = convert(json);

            eventProcessEngine.processOrSkipEvent(eventMessage.body);

        } catch (Exception e) {
            processError(json, e, MessageType.EVENT_PROCESSING_FAILED);
        } finally {
            acknowledge(message);
        }

    }
}

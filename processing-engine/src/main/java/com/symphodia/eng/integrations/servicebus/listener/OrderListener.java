package com.symphodia.eng.integrations.servicebus.listener;

import com.symphodia.eng.core.service.MessageProcessEngine;
import com.symphodia.eng.integrations.servicebus.dto.Message;
import com.symphodia.eng.integrations.servicebus.dto.MessageType;
import com.symphodia.eng.integrations.servicebus.dto.order.OrderBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderListener extends AzureListener {

    @Autowired
    MessageProcessEngine messageProcessEngine;

    @Override
    public void onMessage(javax.jms.Message message) {
        String json = null;
        try {
            json = getMessageContent(message);

            logger.debug("Received message from order listener: {}", json);

            Message<OrderBody> orderMessage = convert(json);

            messageProcessEngine.processOrder(orderMessage);

        } catch (Exception e) {
            processError(json, e, MessageType.ORDER_PROCESSING_FAILED);
        } finally {
            acknowledge(message);
        }
    }

}

package com.symphodia.eng.core.service;

import com.symphodia.eng.integrations.servicebus.dto.Message;
import com.symphodia.eng.integrations.servicebus.dto.order.OrderBody;
import com.symphodia.spring.common.graph.MessageProcess;
import com.symphodia.spring.common.graph.MessageProcessRegistry;
import com.symphodia.spring.common.graph.Outcome;
import com.symphodia.spring.common.log.AutowiredLogger;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageProcessEngine {

    @AutowiredLogger
    Logger logger;

    @Autowired
    MessageProcessRegistry messageProcessRegistry;

    @SuppressWarnings("unchecked")
    public Outcome processOrder(Message<? extends OrderBody> message) {

        MessageProcess businessProcess = messageProcessRegistry.lookup(message.body);

        return businessProcess.process(message);
    }
}

package com.symphodia.eng.integrations.servicebus.service;

import com.symphodia.spring.common.error.ErrorCode;
import com.symphodia.eng.integrations.servicebus.dto.Message;
import com.symphodia.eng.integrations.servicebus.dto.MessageType;
import com.symphodia.eng.integrations.servicebus.dto.event.ErrorEvent;
import com.symphodia.eng.integrations.servicebus.dto.event.ExceptionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ErrorService {

    @Autowired
    MessageProducer messageProducer;

    @Autowired
    MessageService messageService;

    public void processException(String request, Exception e, MessageType generatedMessageType) {
        ErrorEvent errorEvent = new ExceptionEvent();
        errorEvent.errorMessage = e.getMessage();
        errorEvent.error = ErrorCode.getErrorCode(e).name();
        errorEvent.errorCode = ErrorCode.getErrorCode(e).getErrorNumber();
        errorEvent.request = request;

        Message<ErrorEvent> errorMessage = new MessageBuilder<ErrorEvent>()
                .setBody(errorEvent)
                .buildHeader(generatedMessageType, messageService.getCorrelationId(request))
                .build();

        messageProducer.sendEvent(errorMessage);
    }
}

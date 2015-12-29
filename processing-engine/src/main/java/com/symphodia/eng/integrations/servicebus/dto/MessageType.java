package com.symphodia.eng.integrations.servicebus.dto;

import com.google.common.reflect.TypeToken;
import com.symphodia.eng.integrations.servicebus.dto.event.ExceptionEvent;
import com.symphodia.eng.integrations.servicebus.dto.event.OrderCreatedEvent;
import com.symphodia.eng.integrations.servicebus.dto.order.CreateOrder;

import java.lang.reflect.Type;

public enum MessageType {
    ORDER(new TypeToken<Message<CreateOrder>>() {}.getType()),
    ORDER_CREATED(new TypeToken<Message<OrderCreatedEvent>>() {}.getType()),
    EVENT_PROCESSING_FAILED(new TypeToken<Message<ExceptionEvent>>() {}.getType()),
    ORDER_PROCESSING_FAILED(new TypeToken<Message<ExceptionEvent>>() {}.getType());

    private final Type messageType;

    MessageType(Type messageType) {
        this.messageType = messageType;
    }

    public Type getMessageType() {
        return messageType;
    }
}

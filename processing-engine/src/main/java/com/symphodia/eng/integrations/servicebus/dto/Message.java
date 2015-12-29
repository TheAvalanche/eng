package com.symphodia.eng.integrations.servicebus.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class Message<T extends MessageBody> implements com.symphodia.spring.common.graph.Message {

    @NotNull
    @Valid
    public MessageHeader header;

    @NotNull
    @Valid
    public T body;
}

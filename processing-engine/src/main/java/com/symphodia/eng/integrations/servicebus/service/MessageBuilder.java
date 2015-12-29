package com.symphodia.eng.integrations.servicebus.service;

import com.symphodia.eng.integrations.servicebus.dto.*;

import java.time.LocalDateTime;

public class MessageBuilder<T extends MessageBody> {

    public Message<T> message;

    public MessageBuilder() {
        this.message = new Message<>();
    }

    public MessageBuilder<T> setBody(T body) {
        message.body = body;
        return this;
    }

    public MessageBuilder<T> buildHeader(MessageType messageType, String correlationId) {
        MessageHeader header = new MessageHeader();
        header.messageType = messageType;
        header.correlationId = correlationId;
        header.sendTimestamp = LocalDateTime.now();
        header.messageTypeVersion = "1";
        header.channelId = "web";
        header.sender = "eng";
        Authorization authorization = new Authorization();
        authorization.key = "key";
        authorization.secret = "secret";
        header.authorization = authorization;

        message.header = header;
        return this;
    }

    public Message<T> build() {
        return message;
    }

}

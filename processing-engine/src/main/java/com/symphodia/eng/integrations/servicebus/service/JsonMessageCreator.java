package com.symphodia.eng.integrations.servicebus.service;

import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

public class JsonMessageCreator implements MessageCreator {

    public String json;

    public JsonMessageCreator(String json) {
        this.json = json;
    }

    @Override
    public Message createMessage(Session session) throws JMSException {
        TextMessage message = session.createTextMessage();
        message.setText(json);
        return message;
    }

    public String getJson() {
        return json;
    }
}

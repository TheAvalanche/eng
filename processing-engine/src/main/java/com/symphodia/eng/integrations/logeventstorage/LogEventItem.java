package com.symphodia.eng.integrations.logeventstorage;

import java.util.Date;

public class LogEventItem {

    private final   Date eventTime;
    private String body;
    private MessageStatus messageStatus;
    private String source;
    private String destination;

    public LogEventItem() {
        eventTime = new Date();
    }

    public Date getEventTime() {
        return eventTime;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public MessageStatus getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(MessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}

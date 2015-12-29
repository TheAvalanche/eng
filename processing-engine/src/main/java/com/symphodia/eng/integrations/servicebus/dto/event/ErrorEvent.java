package com.symphodia.eng.integrations.servicebus.dto.event;

public abstract class ErrorEvent extends MessageEvent {
    public Integer errorCode;
    public String error;
    public String errorMessage;
    public String request;
}

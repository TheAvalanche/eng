package com.symphodia.eng.integrations.servicebus.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class MessageHeader {

    @NotNull
    public MessageType messageType;
    @NotNull
    public String messageTypeVersion;
    @NotNull
    public String correlationId;
    @NotNull
    public String channelId;
    @NotNull
    public String sender;
    @NotNull
    public LocalDateTime sendTimestamp;
    @NotNull
    @Valid
    public Authorization authorization;
}

package com.symphodia.eng.integrations.servicebus.dto.event;

import com.symphodia.eng.integrations.servicebus.dto.MessageBody;
import com.symphodia.spring.common.graph.Event;

public abstract class MessageEvent extends MessageBody implements Event {
    public String orderId;
}

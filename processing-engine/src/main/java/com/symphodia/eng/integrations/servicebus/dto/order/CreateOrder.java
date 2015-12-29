package com.symphodia.eng.integrations.servicebus.dto.order;

import com.symphodia.spring.common.graph.ProcessKey;
import com.symphodia.eng.core.graph.MessageProcessKey;
import com.symphodia.eng.integrations.servicebus.dto.MessageType;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class CreateOrder extends OrderBody {

    @NotNull
    @Valid
    public Order order;

    @Override
    public String getId() {
        return order.orderId;
    }

    @Override
    public ProcessKey getProcessKey() {
        return new MessageProcessKey(MessageType.ORDER);
    }
}

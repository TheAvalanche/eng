package com.symphodia.eng.core.command;

import com.symphodia.eng.domain.Order;
import com.symphodia.eng.integrations.servicebus.dto.Message;
import com.symphodia.eng.integrations.servicebus.dto.MessageType;
import com.symphodia.eng.integrations.servicebus.dto.event.MessageEvent;
import com.symphodia.eng.integrations.servicebus.dto.event.OrderCreatedEvent;
import com.symphodia.eng.integrations.servicebus.service.MessageBuilder;
import com.symphodia.eng.integrations.servicebus.service.MessageProducer;
import com.symphodia.eng.integrations.servicebus.service.MessageService;
import com.symphodia.spring.common.graph.Outcome;
import com.symphodia.spring.common.graph.ProcessCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendOrderCreatedEventCmd implements ProcessCommand<Order, MessageEvent> {

    @Autowired
    MessageService messageService;

    @Autowired
    MessageProducer messageProducer;

    @Override
    public Outcome invoke(Order order, MessageEvent messageEvent) {
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        orderCreatedEvent.orderId = order.getOrderId();

        Message<OrderCreatedEvent> eventMessage = new MessageBuilder<OrderCreatedEvent>()
                .setBody(orderCreatedEvent)
                .buildHeader(MessageType.ORDER_CREATED, order.getCorrelationId())
                .build();

        messageProducer.sendEvent(eventMessage);

        return Outcome.success();
    }
}

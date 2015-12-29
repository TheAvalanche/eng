package com.symphodia.eng.core.process.order;

import com.symphodia.eng.core.graph.OrderStatus;
import com.symphodia.eng.core.graph.MessageProcessKey;
import com.symphodia.eng.core.service.EventProcessEngine;
import com.symphodia.eng.domain.Order;
import com.symphodia.eng.domain.mapper.DomainMapper;
import com.symphodia.eng.integrations.servicebus.dto.Message;
import com.symphodia.eng.integrations.servicebus.dto.MessageType;
import com.symphodia.eng.integrations.servicebus.dto.order.CreateOrder;
import com.symphodia.eng.repository.OrderRepository;
import com.symphodia.spring.common.graph.MessageProcess;
import com.symphodia.spring.common.graph.Outcome;
import com.symphodia.spring.common.graph.ProcessKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrationNewProcess implements MessageProcess<Message<CreateOrder>> {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    EventProcessEngine eventProcessEngine;

    @Override
    public ProcessKey getKey() {
        return new MessageProcessKey(MessageType.ORDER);
    }

    @Override
    public Outcome process(Message<CreateOrder> message) {
        Order domainOrder = DomainMapper.mapToDomain(message.body.order);
        domainOrder.setCorrelationId(message.header.correlationId);
        domainOrder.setStatus(OrderStatus.NEW);
        orderRepository.save(domainOrder);
        eventProcessEngine.initiate(domainOrder);
        return Outcome.success();
    }
}

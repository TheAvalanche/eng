package com.symphodia.eng.core.process.event;


import com.symphodia.eng.core.graph.EventProcessKey;
import com.symphodia.eng.domain.OrderType;
import com.symphodia.eng.domain.item.ServiceType;
import com.symphodia.eng.integrations.servicebus.dto.event.OrderCreatedEvent;
import com.symphodia.spring.common.graph.EventProcess;
import com.symphodia.spring.common.graph.Graph;
import com.symphodia.spring.common.graph.ProcessKey;
import org.springframework.stereotype.Component;

import static com.symphodia.eng.core.graph.OrderStatus.*;
import static com.symphodia.spring.common.graph.Commands.noCommand;
import static com.symphodia.spring.common.graph.FailureTransition.onFailure;
import static com.symphodia.spring.common.graph.Graph.graph;
import static com.symphodia.spring.common.graph.OnEvent.onEvent;
import static com.symphodia.spring.common.graph.StatusNode.statusNode;
import static com.symphodia.spring.common.graph.SuccessTransition.onSuccess;

@Component
public class OrderNewProcess implements EventProcess {

    @Override
    public ProcessKey getKey() {
        return new EventProcessKey(OrderType.REGULAR, ServiceType.NEW);
    }

    @Override
    public Graph getGraph() {
        return graph(
                statusNode(
                        NEW,
                        onEvent(OrderCreatedEvent.class,
                                noCommand(),
                                onSuccess(ACTIVATED),
                                onFailure(FAILED_ACTIVATION_CANCEL))));
    }

}

package com.symphodia.eng.core.service;


import com.symphodia.eng.core.command.SendOrderCreatedEventCmd;
import com.symphodia.eng.core.graph.OrderStatus;
import com.symphodia.eng.domain.Order;
import com.symphodia.eng.integrations.servicebus.dto.event.MessageEvent;
import com.symphodia.eng.integrations.servicebus.dto.event.SkippableEvent;
import com.symphodia.eng.integrations.servicebus.service.MessageProducer;
import com.symphodia.eng.repository.OrderRepository;
import com.symphodia.spring.common.graph.*;
import com.symphodia.spring.common.log.AutowiredLogger;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class EventProcessEngine {

    @AutowiredLogger
    Logger logger;

    @Autowired
    MessageProducer messageProducer;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    EventProcessRegistry eventProcessRegistry;

    public void initiate(Order order) {
        logger.debug("Initiating processes for order: {}", order.getId());
        applicationContext.getBean(SendOrderCreatedEventCmd.class).invoke(order, null);
    }

    @Transactional
    public Outcome processOrSkipEvent(MessageEvent messageEvent) {
        if (isProcessable(messageEvent)) {
            return processEvent(messageEvent);
        }
        logger.warn("Skipping messageEvent: " + messageEvent);

        return Outcome.skipped();
    }

    @Transactional
    public Outcome processEvent(MessageEvent messageEvent) {
        logger.debug("Processing messageEvent: (orderId: {}, messageEvent: {})", messageEvent.orderId, messageEvent.getClass().getSimpleName());

        Order order = orderRepository.findOne(messageEvent.orderId);
        Enum currentStatus = order.getStatus();

        logger.debug("{} current status: {}", messageEvent.orderId, currentStatus);

        EventProcess businessProcess = eventProcessRegistry.lookup(order);
        OnEvent onEvent = businessProcess.getGraph().getOnEvent(currentStatus, messageEvent.getClass());
        Outcome outcome = runCommands(order, messageEvent, onEvent);
        Enum nextStatus = onEvent.getNextStatus(outcome, currentStatus);
        order.setStatus((OrderStatus) nextStatus);

        logger.debug("{} new status: {}", messageEvent.orderId, nextStatus);

        runPostCommands(order, outcome, messageEvent, onEvent);

        return outcome;
    }

    @SuppressWarnings("unchecked")
    private Outcome runCommands(Order order, MessageEvent messageEvent, OnEvent onEvent) {
        Outcome outcome = Outcome.success();
        for (Class<? extends ProcessCommand> command : onEvent.getCommands().getList()) {
            logger.debug("Going to invoke command for {}: {}", order.getId(), command.getSimpleName());
            outcome = applicationContext.getBean(command).invoke(order, messageEvent);
        }
        return outcome;
    }

    @SuppressWarnings("unchecked")
    private Outcome runPostCommands(Order order, Outcome outcome, MessageEvent messageEvent, OnEvent onEvent) {
        Outcome postOutcome = Outcome.success();
        for (Class<? extends ProcessCommand> command : onEvent.getPostCommands(outcome).getList()) {
            logger.debug("Going to invoke command for {}: {}", order.getId(), command.getSimpleName());
            postOutcome = applicationContext.getBean(command).invoke(order, messageEvent);
        }
        return postOutcome;
    }

    protected boolean isProcessable(MessageEvent messageEvent) {
        return !(messageEvent instanceof SkippableEvent);
    }
}

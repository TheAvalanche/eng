package com.symphodia.eng.integrations.servicebus.listener

import com.google.common.base.Charsets
import com.google.common.io.Resources
import com.symphodia.spring.common.graph.Outcome
import com.symphodia.eng.core.service.EventProcessEngine
import com.symphodia.eng.integrations.servicebus.dto.event.OrderCreatedEvent
import com.symphodia.eng.integrations.servicebus.service.MessageService
import org.slf4j.LoggerFactory
import spock.lang.Specification

import javax.jms.TextMessage
import javax.validation.Validation

class EventListenerTest extends Specification {

    AzureListener eventListener

    MessageService messageService

    EventProcessEngine eventProcessEngine = Mock()

    def setup() {
        messageService = new MessageService(logger: LoggerFactory.getLogger(MessageService.class),
                validator: Validation.buildDefaultValidatorFactory().getValidator())
        eventListener = new EventListener(
                logger: LoggerFactory.getLogger(EventListener.class),
                messageService: messageService,
                eventProcessEngine: eventProcessEngine)
    }

    def "test order created event processing"() {
        setup:
        TextMessage message = Mock()
        message.getText() >> Resources.toString(Resources.getResource("json/order-created-event.json"), Charsets.UTF_8)
        OrderCreatedEvent event
        when:
        eventListener.onMessage(message);
        then:
        1 * eventProcessEngine.processOrSkipEvent(!null) >> { args ->
            event = args[0]
            Outcome.success()
        }
        event.orderId == "M123456"
    }

}
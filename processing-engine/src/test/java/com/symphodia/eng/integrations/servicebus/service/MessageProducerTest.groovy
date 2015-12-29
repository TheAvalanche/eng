package com.symphodia.eng.integrations.servicebus.service

import com.symphodia.spring.common.property.PropertyService
import com.symphodia.eng.integrations.servicebus.dto.Message
import com.symphodia.eng.integrations.servicebus.dto.MessageHeader
import com.symphodia.eng.integrations.servicebus.dto.event.OrderCreatedEvent
import com.symphodia.eng.integrations.servicebus.listener.OrderListener
import org.slf4j.LoggerFactory
import org.springframework.jms.core.JmsTemplate
import spock.lang.Specification

import javax.validation.Validation

class MessageProducerTest extends Specification {

    MessageProducer messageProducer;

    MessageService messageService

    JmsTemplate jmsTemplate = Mock()

    PropertyService propertyService = Mock()

    def "setup"() {
        messageService = new MessageService(logger: LoggerFactory.getLogger(MessageService.class),
                validator: Validation.buildDefaultValidatorFactory().getValidator())
        messageProducer = new MessageProducer(
                logger: LoggerFactory.getLogger(OrderListener.class),
                messageService: messageService,
                jmsTemplate: jmsTemplate,
                propertyService: propertyService)
    }

    def "should convert and send json message"() {
        given:
        Message<OrderCreatedEvent> eventMessage = new Message(
                header: new MessageHeader(correlationId: "123456"),
                body: new OrderCreatedEvent(orderId: "123456"))
        String json
        when:
        messageProducer.sendEvent(eventMessage)
        then:
        1 * jmsTemplate.send(_, _) >> {args -> json = args[1].getJson()}
        json == '''{"header":{"correlationId":"123456"},"body":{"orderId":"123456"}}'''
    }
}

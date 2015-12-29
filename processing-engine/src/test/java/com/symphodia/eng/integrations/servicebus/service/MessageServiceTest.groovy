package com.symphodia.eng.integrations.servicebus.service

import com.google.common.base.CharMatcher
import com.google.common.base.Charsets
import com.google.common.io.Resources
import com.symphodia.eng.core.graph.MessageProcessKey
import com.symphodia.eng.integrations.servicebus.dto.Message
import com.symphodia.eng.integrations.servicebus.dto.MessageType
import com.symphodia.eng.integrations.servicebus.dto.event.OrderCreatedEvent
import com.symphodia.eng.integrations.servicebus.dto.order.CreateOrder
import com.symphodia.eng.integrations.servicebus.dto.order.Card
import com.symphodia.eng.integrations.servicebus.dto.order.Service
import com.symphodia.spring.common.error.ApplicationException
import org.slf4j.LoggerFactory
import spock.lang.Specification

import javax.validation.Validation

class MessageServiceTest extends Specification {

    private MessageService messageService = new MessageService(
            validator: Validation.buildDefaultValidatorFactory().getValidator(),
            logger: LoggerFactory.getLogger(MessageService.class))

    def "should return messasge type"() {
        given:
        String json = Resources.toString(Resources.getResource("json/create-order.json"), Charsets.UTF_8)
        when:
        MessageType messageType = messageService.getMessageType(json)
        then:
        messageType == MessageType.ORDER
    }

    def "test order creation"() {
        given:
        String json = Resources.toString(Resources.getResource("json/create-order.json"), Charsets.UTF_8)
        when:
        Message<CreateOrder> message = messageService.convert(json);
        then:
        message != null
        message.header != null
        message.header.messageType == MessageType.ORDER
        message.body != null
        message.body.id == message.body.order.orderId
        message.body.getProcessKey() == new MessageProcessKey(MessageType.ORDER)
        message.body.order !=
        message.body.order.customer != null
        message.body.order.customer.deliveryAddress != null
        message.body.order.customer.deliveryAddress.city != null
        message.body.order.customer.deliveryAddress.country != null
        message.body.order.customer.deliveryAddress.street != null
        message.body.order.customer.deliveryAddress.zip != null
        message.body.order.products.size() == 2 != null
        message.body.order.getProduct(Card.class) != null
        message.body.order.getProduct(Card.class).id != null
        message.body.order.getProduct(Service.class) != null
        message.body.order.getProduct(Service.class).msisdn != null
        message.body.order.getProduct(Service.class).domain != null
        message.body.order.getProduct(Service.class).serviceType != null

        when:
        String jsonBack = messageService.convert(message)
        then:
        CharMatcher.WHITESPACE.removeFrom(json) == jsonBack
    }

    def "should throw an exception"() {
        when:
        messageService.convert("test")
        then:
        thrown(ApplicationException)
    }

    def "test order created event creation"() {
        when:
        String json = Resources.toString(Resources.getResource("json/order-created-event.json"), Charsets.UTF_8);
        Message<OrderCreatedEvent> message = messageService.convert(json);
        then:
        message != null
        message.header != null
        message.header.messageType == MessageType.ORDER_CREATED
        message.body != null
        message.body.orderId != null

        when:
        String jsonBack = messageService.convert(message);
        then:
        CharMatcher.WHITESPACE.removeFrom(json) == jsonBack
    }

    def "test getting correlation id"() {
        when:
        String json = Resources.toString(Resources.getResource("json/order-created-event.json"), Charsets.UTF_8)
        def correlationId = messageService.getCorrelationId(json)
        then:
        correlationId == "correlationId"

        when:
        correlationId = messageService.getCorrelationId("test")
        then:
        correlationId != null

    }

    def "test message validation"() {
        given:
        String json = Resources.toString(Resources.getResource("json/create-order.json"), Charsets.UTF_8)
        Message<CreateOrder> message = messageService.convert(json)
        message.body.order.customer.deliveryAddress.city = null
        when:
        messageService.validate(message)
        then:
        thrown(ApplicationException)
    }

}
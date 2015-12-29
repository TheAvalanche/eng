package com.symphodia.eng.integrations.servicebus.service
import com.symphodia.eng.integrations.servicebus.dto.Message
import com.symphodia.eng.integrations.servicebus.dto.MessageHeader
import com.symphodia.eng.integrations.servicebus.dto.MessageType
import com.symphodia.eng.integrations.servicebus.dto.event.OrderCreatedEvent
import spock.lang.Specification

class MessageBuilderTest extends Specification {

    def "test message creation"() {

        when:
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        orderCreatedEvent.orderId = "123456"

        Message<OrderCreatedEvent> orderCreatedEventMessage = new MessageBuilder<OrderCreatedEvent>()
                .setBody(orderCreatedEvent)
                .buildHeader(MessageType.ORDER_CREATED, "correlationId").build()

        then:
        MessageHeader messageHeader = orderCreatedEventMessage.header
        messageHeader.messageType == MessageType.ORDER_CREATED
        messageHeader.correlationId == "correlationId"
        messageHeader.channelId == "web"
        messageHeader.messageTypeVersion == "1"
        messageHeader.sender == "eng"
        messageHeader.authorization.key == "key"
        messageHeader.authorization.secret == "secret"
        messageHeader.sendTimestamp != null

        orderCreatedEvent == orderCreatedEventMessage.body

    }
}
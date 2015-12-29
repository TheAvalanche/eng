package com.symphodia.eng.core.command
import com.symphodia.eng.domain.Order
import com.symphodia.eng.integrations.servicebus.dto.Message
import com.symphodia.eng.integrations.servicebus.dto.MessageType
import com.symphodia.eng.integrations.servicebus.dto.event.OrderCreatedEvent
import com.symphodia.eng.integrations.servicebus.service.MessageProducer
import spock.lang.Specification

class SendOrderCreatedEventCmdTest extends Specification {

    MessageProducer messageProducer = Mock()

    SendOrderCreatedEventCmd sendOrderCreatedEventCmd;

    def "setup"() {
        sendOrderCreatedEventCmd = new SendOrderCreatedEventCmd(messageProducer: messageProducer)
    }

    def "test event created on invoke"() {
        given:
        Order order = new Order(orderId: "123456")
        Message<OrderCreatedEvent> orderCreatedEventMessage
        when:
        sendOrderCreatedEventCmd.invoke(order, null);
        then:
        1 * messageProducer.sendEvent(!null) >> {args -> orderCreatedEventMessage = args[0]}
        "123456" == orderCreatedEventMessage.body.orderId
        orderCreatedEventMessage.header.messageType == MessageType.ORDER_CREATED

    }

}
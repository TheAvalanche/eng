package com.symphodia.eng.integrations.servicebus.listener

import com.google.common.base.Charsets
import com.google.common.io.Resources
import com.symphodia.eng.core.service.MessageProcessEngine
import com.symphodia.eng.integrations.servicebus.dto.Message
import com.symphodia.eng.integrations.servicebus.dto.MessageType
import com.symphodia.eng.integrations.servicebus.dto.order.CreateOrder
import com.symphodia.eng.integrations.servicebus.service.MessageService
import com.symphodia.spring.common.graph.Outcome
import org.slf4j.LoggerFactory
import spock.lang.Specification

import javax.jms.TextMessage
import javax.validation.Validation

class OrderListenerTest extends Specification {

    OrderListener orderListener;

    MessageService messageService

    MessageProcessEngine orderProcessEngine = Mock()

    def "setup"() {
        messageService = new MessageService(logger: LoggerFactory.getLogger(MessageService.class),
                validator: Validation.buildDefaultValidatorFactory().getValidator())
        orderListener = new OrderListener(
                logger: LoggerFactory.getLogger(OrderListener.class),
                messageService: messageService,
                messageProcessEngine: orderProcessEngine);
    }

    def "test order sent for processing"() throws Exception {
        setup:
        TextMessage message = Mock(TextMessage.class);
        message.getText() >> Resources.toString(Resources.getResource("json/create-order.json"), Charsets.UTF_8)
        Message<CreateOrder> createOrderMessage
        when:
        orderListener.onMessage(message);
        then:
        1 * orderProcessEngine.processOrder(!null) >> { args ->
            createOrderMessage = args[0]
            Outcome.success()
        }

        createOrderMessage.header.messageType == MessageType.ORDER
        createOrderMessage.header.correlationId == "correlationId"
        createOrderMessage.body.order.orderId == "M123456"
        createOrderMessage.body.order.customer.deliveryAddress.city == "City"
    }

}
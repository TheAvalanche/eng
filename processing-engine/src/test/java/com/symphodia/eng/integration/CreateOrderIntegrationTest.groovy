package com.symphodia.eng.integration

import com.google.common.base.Charsets
import com.google.common.io.Resources
import com.symphodia.eng.TestApplication
import com.symphodia.spring.common.property.PropertyService
import com.symphodia.eng.core.graph.OrderStatus
import com.symphodia.eng.domain.OrderType
import com.symphodia.eng.integrations.servicebus.dto.Message
import com.symphodia.eng.integrations.servicebus.dto.MessageType
import com.symphodia.eng.integrations.servicebus.dto.event.ErrorEvent
import com.symphodia.eng.integrations.servicebus.listener.EventListener
import com.symphodia.eng.integrations.servicebus.listener.OrderListener
import com.symphodia.eng.integrations.servicebus.service.MessageProducer
import com.symphodia.eng.integrations.servicebus.service.MessageService
import com.symphodia.eng.repository.OrderRepository
import com.symphodia.spring.common.error.ErrorCode
import org.apache.qpid.amqp_1_0.jms.impl.SessionImpl
import org.apache.qpid.amqp_1_0.jms.impl.TextMessageImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jms.core.JmsTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import javax.jms.Session
import javax.jms.TextMessage

@ContextConfiguration(classes = [TestApplication.class])
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:test.properties")
class CreateOrderIntegrationTest extends Specification {

    @Autowired
    OrderListener orderListener

    @Autowired
    EventListener eventListener

    @Autowired
    OrderRepository orderRepository

    @Autowired
    MessageProducer messageProducer

    @Autowired
    MessageService messageService

    @Autowired
    PropertyService propertyService

    JmsTemplate jmsTemplate = Mock()
    Session session = Mock()

    def setup() {
        session.createTextMessage() >> new TextMessageImpl(Mock(SessionImpl))

        messageProducer.jmsTemplate = jmsTemplate
        jmsTemplate.send(_, _) >> {args->sendToListener(eventListener, args[1])}

    }

    def sendToListener(listener, messageCreator) {
        Thread.start {
            sleep(100)
            listener.onMessage(messageCreator.createMessage(session))
        }
    }

    def "test regular order creation"() {
        setup:
        TextMessage message = Mock(TextMessage);
        message.getText() >> Resources.toString(Resources.getResource("json/create-order.json"), Charsets.UTF_8)

        when:
        orderListener.onMessage(message)
        def order = orderRepository.findOne("M123456")

        then:
        order != null
        order.orderItems.get(0).order != null
        order.orderItems.size() == 2
        order.status == OrderStatus.NEW
        order.getOrderType() == OrderType.REGULAR

        when:
        Thread.sleep(2000L);
        order = orderRepository.findOne("M123456");

        then:
        order.getStatus() == OrderStatus.SUBSCRIPTION_CREATED
    }

    def "test sample order creation"() {
        setup:
        TextMessage message = Mock(TextMessage);
        message.getText() >> Resources.toString(Resources.getResource("json/activate-sample-order.json"), Charsets.UTF_8)

        when:
        orderListener.onMessage(message)
        def order = orderRepository.findOne("M123456")

        then:
        order != null
        order.orderItems.get(0).order != null
        order.orderItems.size() == 2
        order.status == OrderStatus.NEW
        order.getOrderType() == OrderType.CUSTOM

        when:
        Thread.sleep(1000L);
        order = orderRepository.findOne("M123456");

        then:
        order.getStatus() == OrderStatus.SUBSCRIPTION_ACTIVATED
    }

    def "test order failed"() {
        setup:
        Message<ErrorEvent> errorMessage
        TextMessage message = Mock(TextMessage)
        message.getText() >> "test"

        when:
        orderListener.onMessage(message)
        Thread.sleep(1000L)

        then:
        1 * jmsTemplate.send(_, _) >> { args -> errorMessage = messageService.convert(args[1].getJson()) }
        errorMessage.header.messageType == MessageType.ORDER_PROCESSING_FAILED
        errorMessage.body.error == ErrorCode.PARSE_ERROR.name()
        errorMessage.body.errorCode == ErrorCode.PARSE_ERROR.errorNumber
    }

}
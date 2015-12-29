package com.symphodia.eng.integrations.logeventstorage

import com.symphodia.eng.integrations.servicebus.listener.EventListener
import com.symphodia.eng.integrations.servicebus.service.JsonMessageCreator
import org.aspectj.lang.JoinPoint
import org.slf4j.LoggerFactory
import spock.lang.Specification

import javax.jms.TextMessage

class MessageInterceptorTest extends Specification {

    MessageInterceptor eventsInterceptor

    LogEventsStorage logEventsStorage = Mock()

    def "setup"() {
        eventsInterceptor = new MessageInterceptor(
                logger: LoggerFactory.getLogger(MessageInterceptor.class),
                eventsLogStorage: logEventsStorage)
    }

    def "test before receive method"() {
        setup:
        TextMessage message = Mock()
        message.getText() >> "Hello"
        JoinPoint jp = Mock()
        jp.getTarget() >> new EventListener()
        LogEventItem log
        when:
        eventsInterceptor.beforeReceive(jp, message)
        then:
        1 * logEventsStorage.writeEvent(_) >> {args -> log = args[0]}

        log.body == "Hello"
        log.messageStatus == MessageStatus.OK
        log.source == "EventListener"
        log.destination == null
        log.eventTime != null
    }

    def "test before send message"() {
        setup:
        JsonMessageCreator messageCreator = new JsonMessageCreator("{hello}")
        LogEventItem log
        when:
        eventsInterceptor.beforeSend(null, "destination", messageCreator)
        then:
        1 * logEventsStorage.writeEvent(_) >> {args -> log = args[0]}

        log.body == "{hello}"
        log.messageStatus == MessageStatus.OK
        log.source == null
        log.destination == "destination"
        log.eventTime != null

    }

    def "test handle exception"() {
        setup:
        JoinPoint jp = Mock()
        jp.getTarget() >> new EventListener()
        LogEventItem log
        when:
        eventsInterceptor.handlerError(jp, new RuntimeException("Error"))
        then:
        logEventsStorage.writeEvent(!null) >> {args -> log = args[0]}

        log.getBody().contains("Error")
        log.getMessageStatus() == MessageStatus.EXCEPTION
        log.getEventTime() != null
    }

}
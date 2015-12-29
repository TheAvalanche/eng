package com.symphodia.eng.integrations.servicebus.service

import org.apache.qpid.amqp_1_0.jms.impl.TextMessageImpl
import spock.lang.Specification

import javax.jms.Session
import javax.jms.TextMessage

class JsonMessageCreatorTest extends Specification {

    def "should return setted text"() {
        given:
        def session = Mock(Session)
        def json = '{"test":"test"}'
        def jsonMessageCreator = new JsonMessageCreator(json);

        when:
        session.createTextMessage() >> new TextMessageImpl(null)
        TextMessage textMessage = jsonMessageCreator.createMessage(session)

        then:
        textMessage.getText() == json
    }
}
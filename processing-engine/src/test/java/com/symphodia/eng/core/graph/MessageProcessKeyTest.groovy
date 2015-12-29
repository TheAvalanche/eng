package com.symphodia.eng.core.graph

import com.symphodia.eng.integrations.servicebus.dto.MessageType
import com.symphodia.spring.common.graph.ProcessKey
import spock.lang.Specification

class MessageProcessKeyTest extends Specification {

    def "test equals"() {
        when:
        ProcessKey processKey = new MessageProcessKey(MessageType.ORDER)
        then:
        !processKey.equals(null)
        !processKey.equals(new Object())
        processKey.equals(processKey)
        processKey.equals(new MessageProcessKey(MessageType.ORDER))
        !processKey.equals(new MessageProcessKey(MessageType.ORDER_CREATED))
    }

    def "test toString"() {
        when:
        ProcessKey processKey = new MessageProcessKey(MessageType.ORDER)
        then:
        processKey.toString() == "MessageProcessKey{ORDER}"
    }

}
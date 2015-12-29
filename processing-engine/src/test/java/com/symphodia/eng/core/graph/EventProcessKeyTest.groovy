package com.symphodia.eng.core.graph

import com.symphodia.eng.domain.OrderType
import com.symphodia.eng.domain.item.ServiceType
import com.symphodia.spring.common.graph.ProcessKey
import spock.lang.Specification

class EventProcessKeyTest extends Specification {

    def "test equals"() {
        when:
        ProcessKey processKey = new EventProcessKey(OrderType.REGULAR, ServiceType.NEW);
        then:
        !processKey.equals(null)
        !processKey.equals(new Object())
        processKey.equals(processKey)
        processKey.equals(new EventProcessKey(OrderType.REGULAR, ServiceType.NEW))
        !processKey.equals(new EventProcessKey(OrderType.REGULAR, ServiceType.OLD))
        !processKey.equals(new EventProcessKey(OrderType.CUSTOM, ServiceType.OLD))
    }

    def "test to string"() {
        when:
        ProcessKey processKey = new EventProcessKey(OrderType.REGULAR, ServiceType.NEW);
        then:
        processKey.toString() == "EventProcessKey{REGULAR, NEW}"
    }

}
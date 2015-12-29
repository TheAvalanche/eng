package com.symphodia.eng.integrations.servicebus.adapter
import com.google.gson.JsonPrimitive
import spock.lang.Specification

import java.time.LocalDateTime

class LocalDateTimeAdapterTest extends Specification {

    def "test serialize"() {
        when:
        LocalDateTimeAdapter adapter = new LocalDateTimeAdapter()
        LocalDateTime dest = LocalDateTime.of(2015, 1, 1, 12, 12, 12)
        LocalDateTime src = adapter.deserialize(new JsonPrimitive("2015-01-01T12:12:12"), null, null)
        then:
        dest == src
    }

    def "test deserialize"() {
        when:
        LocalDateTimeAdapter adapter = new LocalDateTimeAdapter()
        String dest = "2015-01-01T12:12:12"
        String src = adapter.serialize(LocalDateTime.of(2015, 1, 1, 12, 12, 12), null, null).getAsString()
        then:
        dest == src
    }

}
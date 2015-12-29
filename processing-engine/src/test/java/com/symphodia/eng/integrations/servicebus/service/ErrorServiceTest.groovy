package com.symphodia.eng.integrations.servicebus.service

import com.symphodia.eng.integrations.servicebus.dto.MessageType
import com.symphodia.spring.common.error.ApplicationException
import com.symphodia.spring.common.error.ErrorCode
import spock.lang.Specification

class ErrorServiceTest extends Specification {

    MessageProducer messageProducer = Mock(MessageProducer)

    MessageService messageService = Mock(MessageService)

    ErrorService errorService

    def setup() {
        errorService = new ErrorService(messageProducer: messageProducer, messageService: messageService);
    }

    def "should send error event"() {
        def errorMessage

        when:
        errorService.processException("test", new ApplicationException(ErrorCode.INTERNAL_SERVICE_ERROR, "Error"), MessageType.EVENT_PROCESSING_FAILED)

        then:
        1 * messageProducer.sendEvent(!null) >> { args -> errorMessage = args[0] }
        errorMessage.body.request == "test"
        errorMessage.body.errorMessage == "Error"
        errorMessage.body.error == ErrorCode.INTERNAL_SERVICE_ERROR.name()
        errorMessage.body.errorCode.intValue() == ErrorCode.INTERNAL_SERVICE_ERROR.errorNumber
        errorMessage.header.messageType == MessageType.EVENT_PROCESSING_FAILED
    }

}
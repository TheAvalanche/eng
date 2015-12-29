package com.symphodia.eng.core.service
import com.symphodia.eng.core.graph.MessageProcessKey
import com.symphodia.eng.integrations.servicebus.dto.Message
import com.symphodia.eng.integrations.servicebus.dto.MessageType
import com.symphodia.eng.integrations.servicebus.dto.order.CreateOrder
import com.symphodia.eng.integrations.servicebus.dto.order.OrderBody
import com.symphodia.spring.common.graph.MessageProcess
import com.symphodia.spring.common.graph.MessageProcessRegistry
import com.symphodia.spring.common.graph.Outcome
import com.symphodia.spring.common.graph.ProcessKey
import org.slf4j.LoggerFactory
import spock.lang.Specification

class MessageProcessEngineTest extends Specification {

    MessageProcessRegistry orderProcessRegistry = Mock()

    DefaultMessageProcess orderProcess = Mock()

    MessageProcessEngine eventProcessEngine

    def setup() {
        orderProcessRegistry.lookup(_) >> orderProcess

        eventProcessEngine = new MessageProcessEngine(
                logger: LoggerFactory.getLogger(MessageProcessEngine.class),
                messageProcessRegistry: orderProcessRegistry)
    }

    def "test execution"() {
        given:
        OrderBody orderBody = new CreateOrder();
        Message<OrderBody> message = new Message<>();
        message.body = orderBody;
        when:
        eventProcessEngine.processOrder(message);
        then:
        1 * orderProcess.process(message);
    }

    class DefaultMessageProcess implements MessageProcess {

        public ProcessKey getKey() {
            return new MessageProcessKey(MessageType.ORDER);
        }

        public Outcome process(com.symphodia.spring.common.graph.Message order) {
            return Outcome.success();
        }
    }

}
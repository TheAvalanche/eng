package com.symphodia.eng.core.service

import com.symphodia.eng.core.graph.EventProcessKey
import com.symphodia.eng.core.graph.OrderStatus
import com.symphodia.eng.domain.Order
import com.symphodia.eng.domain.OrderType
import com.symphodia.eng.domain.item.ServiceType
import com.symphodia.eng.integrations.servicebus.dto.event.OrderActivatedEvent
import com.symphodia.eng.integrations.servicebus.dto.event.OrderCreatedEvent
import com.symphodia.eng.integrations.servicebus.service.MessageProducer
import com.symphodia.eng.repository.OrderRepository
import com.symphodia.spring.common.error.ApplicationException
import com.symphodia.spring.common.error.ErrorCode
import com.symphodia.spring.common.graph.*
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import spock.lang.Specification

import static com.symphodia.eng.core.graph.OrderStatus.*
import static com.symphodia.spring.common.graph.Commands.commands
import static com.symphodia.spring.common.graph.FailureTransition.onFailure
import static com.symphodia.spring.common.graph.OnEvent.onEvent
import static com.symphodia.spring.common.graph.StatusNode.inAnyStatus
import static com.symphodia.spring.common.graph.StatusNode.statusNode
import static com.symphodia.spring.common.graph.SuccessTransition.onSuccess

class EventProcessEngineTest extends Specification {

    OrderRepository orderRepository = Mock()

    EventProcessRegistry eventProcessRegistry = Mock()

    MessageProducer messageProducer = Mock()

    ApplicationContext applicationContext = Mock()

    EventProcessEngine eventProcessEngine

    def setup() {

        Order order = new Order(orderId: "123456", status: OrderStatus.NEW, orderType: OrderType.REGULAR)

        orderRepository.findOne(_) >> order
        eventProcessRegistry.lookup(_) >> new DefaultEventProcess()
        applicationContext.getBean(SuccessCommand.class) >> new SuccessCommand()
        applicationContext.getBean(FailureCommand.class) >> new FailureCommand()

        eventProcessEngine = new EventProcessEngine(
            eventProcessRegistry: eventProcessRegistry,
            orderRepository: orderRepository,
            messageProducer: messageProducer,
            applicationContext: applicationContext,
            logger: LoggerFactory.getLogger(EventProcessEngine.class))
    }

    def "test successful execution"() {
        when:
        eventProcessEngine.processOrSkipEvent(new OrderCreatedEvent(orderId: "123456"))
        then:
        orderRepository.findOne("123456").getStatus() == OrderStatus.VALIDATED

    }

    def "test failure execution"() {
        when:
        eventProcessEngine.processEvent(new OrderActivatedEvent(orderId: "123456"))
        then:
        orderRepository.findOne("123456").getStatus() == OrderStatus.FAILED_VALIDATION

    }

    class DefaultEventProcess implements EventProcess {

        public ProcessKey getKey() {
            return new EventProcessKey(OrderType.REGULAR, ServiceType.NEW);
        }

        public Graph getGraph() {
            return graph(
                    statusNode(
                            NEW,
                            onEvent(OrderCreatedEvent.class,
                                    commands(SuccessCommand.class),
                                    onSuccess(VALIDATED, SuccessCommand.class),
                                    onFailure(FAILED_VALIDATION, FailureCommand.class)),
                            onEvent(OrderCreatedEvent.class,
                                    commands(SuccessCommand.class),
                                    onSuccess(ACTIVATED))),
                    inAnyStatus(
                            onEvent(OrderActivatedEvent.class,
                                    commands(FailureCommand.class),
                                    onSuccess(PLACED),
                                    onFailure(FAILED_VALIDATION))
                    )
            );
        }

    }

    class SuccessCommand implements ProcessCommand {
        public Outcome invoke(ProcessableItem processableItem, Event messageEvent) {
            return Outcome.success();
        }
    }

    class FailureCommand implements ProcessCommand {
        public Outcome invoke(ProcessableItem processableItem, Event messageEvent) {
            return Outcome.failure(new ApplicationException(ErrorCode.INTERNAL_SERVICE_ERROR, "Some error"));
        }
    }

}
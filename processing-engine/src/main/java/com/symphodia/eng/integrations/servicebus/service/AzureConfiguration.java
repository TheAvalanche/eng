package com.symphodia.eng.integrations.servicebus.service;

import com.symphodia.spring.common.error.ApplicationException;
import com.symphodia.spring.common.error.ErrorCode;
import com.symphodia.eng.common.property.Property;
import com.symphodia.spring.common.property.PropertyService;
import com.symphodia.eng.integrations.servicebus.listener.EventListener;
import com.symphodia.eng.integrations.servicebus.listener.OrderListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.SimpleMessageListenerContainer;

import javax.jms.ConnectionFactory;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;

import static com.symphodia.eng.common.property.Property.AZURE_ENDPOINT;

@Configuration
@Profile({"dev", "sit", "prod"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AzureConfiguration {

    @Autowired
    PropertyService propertyService;

    @Autowired
    EventListener eventListener;

    @Autowired
    OrderListener orderListener;

    public ConnectionFactory connectionFactory() {
        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.qpid.amqp_1_0.jms.jndi.PropertiesFileInitialContextFactory");
            env.put("connectionfactory.AZURE_CF", propertyService.getValue(AZURE_ENDPOINT));
            Context context = new InitialContext(env);
            return (ConnectionFactory) context.lookup("AZURE_CF");
        } catch (Exception exc) {
            throw new ApplicationException(ErrorCode.INTERNAL_SERVICE_ERROR, "Error to init connection factory", exc);
        }
    }

    @Bean
    public SimpleJmsListenerContainerFactory jmsListenerContainerFactory() {
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setSubscriptionDurable(true);
        factory.setSessionTransacted(false);
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(connectionFactory());
    }

    @Bean
    public SimpleMessageListenerContainer serviceBusOrderMessageListenerContainer() {
        SimpleMessageListenerContainer orderListenerContainer = new SimpleMessageListenerContainer();
        orderListenerContainer.setConnectionFactory(connectionFactory());
        orderListenerContainer.setClientId("OrderListener");
        orderListenerContainer.setupMessageListener(orderListener);
        orderListenerContainer.setDestinationName(propertyService.getValue(Property.AZURE_ORDER_TOPIC));
        orderListenerContainer.setDurableSubscriptionName(propertyService.getValue(Property.AZURE_SUBSCRIPTION));
        orderListenerContainer.setPubSubDomain(true);
        orderListenerContainer.setSessionTransacted(false);
        return orderListenerContainer;
    }

    @Bean
    public SimpleMessageListenerContainer serviceBusEventMessageListenerContainer() {
        SimpleMessageListenerContainer eventListenerContainer = new SimpleMessageListenerContainer();
        eventListenerContainer.setConnectionFactory(connectionFactory());
        eventListenerContainer.setClientId("EventListener");
        eventListenerContainer.setupMessageListener(eventListener);
        eventListenerContainer.setDestinationName(propertyService.getValue(Property.AZURE_EVENT_TOPIC));
        eventListenerContainer.setDurableSubscriptionName(propertyService.getValue(Property.AZURE_SUBSCRIPTION));
        eventListenerContainer.setPubSubDomain(true);
        eventListenerContainer.setSessionTransacted(false);
        return eventListenerContainer;
    }
}

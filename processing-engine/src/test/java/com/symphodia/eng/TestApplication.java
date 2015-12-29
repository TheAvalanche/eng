package com.symphodia.eng;

import com.symphodia.spring.common.property.PropertyService;
import com.symphodia.eng.integrations.servicebus.listener.EventListener;
import com.symphodia.spring.common.CommonsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan
@EnableJpaRepositories
@EnableAutoConfiguration
@Profile("test")
@Import({CommonsConfig.class})
public class TestApplication {

    @Autowired
    PropertyService propertyService;

    @Autowired
    EventListener eventListener;

    @Bean
    public Validator localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        return mock(JmsTemplate.class);
    }
}

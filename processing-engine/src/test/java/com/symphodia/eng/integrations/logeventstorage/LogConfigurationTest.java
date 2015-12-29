package com.symphodia.eng.integrations.logeventstorage;

import org.elasticsearch.client.Client;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class LogConfigurationTest {

    @Bean
    @Profile("test")
    public Client client() {
        return Mockito.mock(Client.class);
    }

}
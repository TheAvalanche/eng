package com.symphodia.eng.integrations.logeventstorage;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.symphodia.eng.common.property.Property;
import com.symphodia.spring.common.property.PropertyService;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

@Configuration
@Profile({"dev", "sit", "prod"})
public class LogConfiguration {

    @Autowired
    PropertyService propertyService;

    @Bean
    @Profile({"dev", "sit", "prod"})
    public Client client() throws IOException {
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", propertyService.getValue(Property.ELASTIC_SEARCH_CLUSTER)).build();
        Client client = new TransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
        String initScript = Resources.toString(Resources.getResource("initelasticsearch.json"), Charsets.UTF_8);
        Settings settings2 = ImmutableSettings.settingsBuilder().loadFromSource(initScript).build();
        client.admin().indices().prepareCreate("eng").setSettings(settings);
        return client;
    }

    @Bean
    public MessageInterceptor eventsInterceptor() {
        return new MessageInterceptor();
    }
}

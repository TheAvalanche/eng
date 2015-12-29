package com.symphodia.eng.core.service;


import com.symphodia.eng.common.property.Property;
import com.symphodia.spring.common.graph.EventProcessRegistry;
import com.symphodia.spring.common.graph.MessageProcessRegistry;
import com.symphodia.spring.common.property.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ProcessRegistry {

    @Autowired
    EventProcessRegistry eventProcessRegistry;

    @Autowired
    MessageProcessRegistry messageProcessRegistry;

    @Autowired
    PropertyService propertyService;

    @PostConstruct
    public void init() throws Exception {
        eventProcessRegistry.registerAllFromPackage(propertyService.getValue(Property.EVENT_PROCESS_SCAN_PACKAGE));
        messageProcessRegistry.registerAllFromPackage(propertyService.getValue(Property.MESSAGE_PROCESS_SCAN_PACKAGE));
    }
}

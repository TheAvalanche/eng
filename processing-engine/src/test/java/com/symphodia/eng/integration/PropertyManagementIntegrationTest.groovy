package com.symphodia.eng.integration

import com.symphodia.eng.TestApplication
import com.symphodia.eng.common.property.Property
import com.symphodia.eng.common.property.PropertyManagement
import com.symphodia.spring.common.property.PropertyRepository
import com.symphodia.spring.common.property.PropertyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

@ContextConfiguration(classes = [TestApplication.class])
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:test.properties")
class PropertyManagementIntegrationTest extends Specification {

    @Autowired
    PropertyService propertyService;

    @Autowired
    PropertyManagement propertyManagement;

    @Autowired
    PropertyRepository propertyRepository;

    def "test put and update properties"() {
        when:
        propertyService.getValue(Property.EVENT_PROCESS_SCAN_PACKAGE)
        then:
        propertyRepository.findOne(Property.EVENT_PROCESS_SCAN_PACKAGE.name()) != null

        when:
        propertyManagement.putValue(Property.EVENT_PROCESS_SCAN_PACKAGE.name(), "test.property")
        then:
        "test.property" == propertyService.getValue(Property.EVENT_PROCESS_SCAN_PACKAGE)
        "test.property" == propertyRepository.findOne(Property.EVENT_PROCESS_SCAN_PACKAGE.name()).value
        "EVENT_PROCESS_SCAN_PACKAGE : test.property \n" == propertyManagement.cacheContent()

        when:
        propertyManagement.clearCache()
        then:
        propertyManagement.cacheContent() == ""
        propertyRepository.findOne(Property.EVENT_PROCESS_SCAN_PACKAGE.name()).value == "test.property"
    }


}
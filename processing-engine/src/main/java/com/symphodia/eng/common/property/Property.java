package com.symphodia.eng.common.property;

import com.symphodia.spring.common.property.PropertyKey;
import com.symphodia.spring.common.property.PropertyType;

public enum Property implements PropertyKey {

    EVENT_PROCESS_SCAN_PACKAGE("com.symphodia.eng.core.process.event", "Base package for scanning processes on boot", PropertyType.STRING),
    MESSAGE_PROCESS_SCAN_PACKAGE("com.symphodia.eng.core.process.order", "Base package for scanning processes on boot", PropertyType.STRING),
    AZURE_ENDPOINT("amqps://test-accesskey:F34m82ZvDhTaQ9HxbVu5qXl5ebD9v185TjZ28Hbi9jg=@test-ns.servicebus.windows.net", "Azure connection string", PropertyType.STRING),
    AZURE_EVENT_TOPIC("eventtopic", "Topic for processing events", PropertyType.STRING),
    AZURE_ORDER_TOPIC("ordertopic", "Topic for processing orders", PropertyType.STRING),
    AZURE_SUBSCRIPTION("engsub","Azure subscription", PropertyType.STRING),
    ELASTIC_SEARCH_CLUSTER("elasticsearch_test","ElasticSearch cluster name", PropertyType.STRING);

    private String value;

    private String description;

    private PropertyType propertyType;

    Property(String defaultValue, String defaultDescription, PropertyType defaultPropertyType) {
        this.value = defaultValue;
        this.description = defaultDescription;
        this.propertyType = defaultPropertyType;
    }

    public String getName() {
        return name();
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }
}

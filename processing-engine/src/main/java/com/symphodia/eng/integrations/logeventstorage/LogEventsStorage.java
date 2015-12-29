package com.symphodia.eng.integrations.logeventstorage;

import com.symphodia.spring.common.error.ApplicationException;
import com.symphodia.spring.common.error.ErrorCode;
import com.symphodia.spring.common.property.PropertyService;
import com.symphodia.spring.common.log.AutowiredLogger;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogEventsStorage {

    @AutowiredLogger
    Logger logger;

    @Autowired
    PropertyService propertyService;

    @Autowired
    Client client;

    public void writeEvent(LogEventItem logEventItem) {

        try {
            IndexRequestBuilder request = generateRequest(logEventItem);
            request.execute().actionGet();
        } catch (Exception exc) {
            logger.error("Error while store message [" + logEventItem.getBody() + "]in log ");
            throw new ApplicationException(ErrorCode.INTERNAL_SERVICE_ERROR, "Elastic search send message errror", exc);
        }

    }

    private IndexRequestBuilder generateRequest(LogEventItem logEventItem) throws Exception {
        return client.prepareIndex("eng", "event").setSource(
                XContentFactory.jsonBuilder().startObject()
                        .field("body", logEventItem.getBody())
                        .field("eventTime", logEventItem.getEventTime())
                        .field("status", logEventItem.getMessageStatus())
                        .field("source", logEventItem.getSource())
                        .field("destination", logEventItem.getDestination())
        );
    }

    public void cleanLogs() {
        client.prepareDeleteByQuery("eng").
                setQuery(QueryBuilders.matchAllQuery()).
                setTypes("event").
                execute().actionGet();
    }
}

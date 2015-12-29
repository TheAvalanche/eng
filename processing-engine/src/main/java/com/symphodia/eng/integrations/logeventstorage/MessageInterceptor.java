package com.symphodia.eng.integrations.logeventstorage;

import com.symphodia.eng.integrations.servicebus.service.JsonMessageCreator;
import com.symphodia.spring.common.log.AutowiredLogger;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * Created by olegskor on 2015.04.28..
 */

@Aspect
public class MessageInterceptor {

    @AutowiredLogger
    Logger logger;

    @Autowired
    LogEventsStorage eventsLogStorage;


    @Before("(execution(* com.symphodia.eng.integrations.servicebus.listener.EventListener.onMessage(*)) || " +
            "execution(* com.symphodia.eng.integrations.servicebus.listener.OrderListener.onMessage(*))) && " +
            "args(message)")
    public void beforeReceive(JoinPoint jp, Message message) {
        try {
            LogEventItem logEventItem = new LogEventItem();
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                logEventItem.setBody(textMessage.getText());
            }
            logEventItem.setSource(jp.getTarget().getClass().getSimpleName());
            logEventItem.setMessageStatus(MessageStatus.OK);
            eventsLogStorage.writeEvent(logEventItem);
        } catch (Exception e) {
            logger.error("Error to handle message from queue", e);
        }
    }

    @Before("execution(* org.springframework.jms.core.JmsTemplate.send(*,*)) && args(destination, messageCreator)")
    public void beforeSend(JoinPoint jp, String destination, MessageCreator messageCreator) {
        try {
            LogEventItem logEventItem = new LogEventItem();
            if (messageCreator instanceof JsonMessageCreator) {
                JsonMessageCreator jsonMessageCreator = (JsonMessageCreator) messageCreator;
                logEventItem.setBody(jsonMessageCreator.getJson());
            }
            logEventItem.setDestination(destination);
            logEventItem.setMessageStatus(MessageStatus.OK);
            eventsLogStorage.writeEvent(logEventItem);
        } catch (Exception e) {
            logger.error("Error to handle message from queue", e);
        }
    }

    @AfterThrowing(
            pointcut = "within(com.symphodia.eng.integrations.servicebus.listener..*)",
            throwing = "exception"
    )
    public void handlerError(JoinPoint jp, RuntimeException exception) {
        LogEventItem logEventItem = new LogEventItem();
        logEventItem.setBody(ExceptionUtils.getMessage(exception) + " - " + ExceptionUtils.getStackTrace(exception));
        logEventItem.setSource(jp.getTarget().getClass().getSimpleName());
        logEventItem.setMessageStatus(MessageStatus.EXCEPTION);
        eventsLogStorage.writeEvent(logEventItem);
        logger.error("Exception ", exception);

    }


}

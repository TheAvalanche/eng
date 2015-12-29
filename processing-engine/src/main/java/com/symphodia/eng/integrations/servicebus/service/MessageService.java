package com.symphodia.eng.integrations.servicebus.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.symphodia.spring.common.error.ApplicationException;
import com.symphodia.spring.common.error.ErrorCode;
import com.symphodia.eng.integrations.servicebus.adapter.LocalDateTimeAdapter;
import com.symphodia.eng.integrations.servicebus.adapter.ProductAdapter;
import com.symphodia.eng.integrations.servicebus.dto.Message;
import com.symphodia.eng.integrations.servicebus.dto.MessageBody;
import com.symphodia.eng.integrations.servicebus.dto.MessageType;
import com.symphodia.eng.integrations.servicebus.dto.order.Product;
import com.symphodia.spring.common.log.AutowiredLogger;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
public class MessageService {

    @AutowiredLogger
    Logger logger;

    @Autowired
    Validator validator;

    public MessageType getMessageType(String json) {
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        return MessageType.valueOf(jsonObject.get("header").getAsJsonObject().get("messageType").getAsString());
    }

    public <T extends MessageBody> Message<T> convert(String json) {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .registerTypeAdapter(Product.class, new ProductAdapter())
                    .create();
            return gson.fromJson(json, getMessageType(json).getMessageType());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.PARSE_ERROR, "Could not parse json message: " + ExceptionUtils.getMessage(e), e);
        }
    }

    public String convert(Message message) {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .registerTypeAdapter(Product.class, new ProductAdapter())
                    .create();
            return gson.toJson(message);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.PARSE_ERROR, "Could not parse message: " + ExceptionUtils.getMessage(e), e);
        }
    }

    public String getCorrelationId(String json) {
        try {
            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
            return jsonObject.get("header").getAsJsonObject().get("correlationId").getAsString();
        } catch (Exception e) {
            logger.warn("Error during while getting correlation id. Generating new one instead.", e);
            return UUID.randomUUID().toString();
        }
    }

    public void validate(Message message) {
        Set<ConstraintViolation<Message>> violations = validator.validate(message);
        if (violations.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            violations.forEach(v -> stringBuilder.append(String.format("%s : %s\n", v.getPropertyPath(), v.getMessage())));
            throw new ApplicationException(ErrorCode.VALIDATION_ERROR, stringBuilder.toString());
        }
    }
}

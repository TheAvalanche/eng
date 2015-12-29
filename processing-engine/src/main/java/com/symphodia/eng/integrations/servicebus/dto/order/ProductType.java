package com.symphodia.eng.integrations.servicebus.dto.order;

import com.google.common.reflect.TypeToken;

import java.lang.reflect.Type;

public enum ProductType {
    SERVICE(new TypeToken<Service>() {}.getType()),
    CARD(new TypeToken<Card>() {}.getType());

    private final Type productType;

    ProductType(Type productType) {
        this.productType = productType;
    }

    public Type getProductType() {
        return productType;
    }
}

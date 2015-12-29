package com.symphodia.eng.integrations.servicebus.dto.order;

public enum OrderType {
    REGULAR, CUSTOM;

    public com.symphodia.eng.domain.OrderType toDomainType() {
        switch (this) {

            case REGULAR:
                return com.symphodia.eng.domain.OrderType.REGULAR;
            case CUSTOM:
                return com.symphodia.eng.domain.OrderType.CUSTOM;
        }
        return com.symphodia.eng.domain.OrderType.REGULAR;
    }
}

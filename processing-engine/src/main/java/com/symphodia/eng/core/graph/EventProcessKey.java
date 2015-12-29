package com.symphodia.eng.core.graph;


import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.symphodia.spring.common.graph.ProcessKey;
import com.symphodia.eng.domain.OrderType;
import com.symphodia.eng.domain.item.ServiceType;


public class EventProcessKey implements ProcessKey {

    private OrderType orderType;
    private ServiceType serviceType;

    public EventProcessKey(OrderType orderType, ServiceType serviceType) {
        this.orderType = orderType;
        this.serviceType = serviceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventProcessKey that = (EventProcessKey) o;

        return Objects.equal(this.orderType, that.orderType)
                && Objects.equal(this.serviceType, that.serviceType);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.orderType, this.serviceType);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).addValue(orderType).addValue(serviceType).toString();
    }
}

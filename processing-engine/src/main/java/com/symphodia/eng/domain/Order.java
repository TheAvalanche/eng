package com.symphodia.eng.domain;


import com.symphodia.eng.core.graph.EventProcessKey;
import com.symphodia.eng.core.graph.OrderStatus;
import com.symphodia.eng.domain.item.OrderItem;
import com.symphodia.eng.domain.item.ProductType;
import com.symphodia.eng.domain.item.Service;
import com.symphodia.spring.common.graph.ProcessKey;
import com.symphodia.spring.common.graph.ProcessableItem;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDER_DETAIL")
public class Order implements ProcessableItem {

    @Id
    @Column(name = "ID")
    private String orderId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "TYPE")
    private OrderType orderType;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "STATUS")
    private OrderStatus status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "ORDER_ID")
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(name="CORRELATION_ID")
    private String correlationId;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public ProcessKey getProcessKey() {
        return new EventProcessKey(orderType, getOrderItem(Service.class).getServiceType());
    }

    @Override
    public String getId() {
        return orderId;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }


    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        this.orderItems.stream().forEach(p -> p.setOrder(this));
    }

    public OrderItem getOrderItem(ProductType productType) {
        return orderItems.stream().filter(p -> p.getProductType() == productType).findFirst().get();
    }

    public <T extends OrderItem> T getOrderItem(Class<T> type) {
        return orderItems.stream().filter(p -> p.getClass().isAssignableFrom(type)).map(p -> (T) p).findFirst().get();
    }
}

package com.symphodia.eng.integrations.servicebus.dto.order;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class Order {
    @NotNull
    public OrderType orderType;
    @NotNull
    public String orderId;
    @NotNull
    @Valid
    public Customer customer;
    @Size(min = 1)
    @Valid
    public List<Product> products;
    public String correlationId;

    public Product getProduct(ProductType productType) {
        return products.stream().filter(p -> p.productType == productType).findFirst().get();
    }

    public <T extends Product> T getProduct(Class<T> type) {
        return products.stream().filter(p -> p.getClass().isAssignableFrom(type)).map(p -> (T) p).findFirst().get();
    }
}

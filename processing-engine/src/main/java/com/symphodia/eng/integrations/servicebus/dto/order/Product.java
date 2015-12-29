package com.symphodia.eng.integrations.servicebus.dto.order;

import javax.validation.constraints.NotNull;

public abstract class Product {

    @NotNull
    public ProductType productType;
    @NotNull
    public String id;

}

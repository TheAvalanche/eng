package com.symphodia.eng.integrations.servicebus.dto.order;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class Customer {
    @NotNull
    public String firstName;
    @NotNull
    public String lastName;
    @Valid
    public Address deliveryAddress;
    @NotNull
    public String language;
}

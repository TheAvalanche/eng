package com.symphodia.eng.integrations.servicebus.dto.order;

import javax.validation.constraints.NotNull;

public class Address {
    @NotNull
    public String street;
    @NotNull
    public String city;
    @NotNull
    public String zip;
    @NotNull
    public String country;
}

package com.symphodia.eng.domain.item;

import javax.persistence.*;

@Entity
@DiscriminatorValue("SERVICE")
public class Service extends OrderItem {

    @Enumerated(value = EnumType.STRING)
    @Column(name = "ACTIVATION_TYPE")
    private ServiceType serviceType = ServiceType.NEW;

    @Column(name = "DOMAIN")
    private String domain;

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public ProductType getProductType() {
        return ProductType.SERVICE;
    }
}

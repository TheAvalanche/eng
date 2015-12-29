package com.symphodia.eng.domain.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CARD")
public class Card extends OrderItem {

    @Override
    public ProductType getProductType() {
        return ProductType.CARD;
    }
}

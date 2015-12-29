package com.symphodia.eng.domain.mapper;

import com.symphodia.eng.integrations.servicebus.dto.order.Order;
import com.symphodia.eng.integrations.servicebus.dto.order.Card;
import com.symphodia.eng.integrations.servicebus.dto.order.Service;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.ConfigurableMapper;

public class DomainMapper extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {

        factory.classMap(Order.class, com.symphodia.eng.domain.Order.class)
                .exclude("orderType")
                .field("products", "orderItems")
                .customize(new CustomMapper<Order, com.symphodia.eng.domain.Order>() {
                    @Override
                    public void mapAtoB(Order orderDTO, com.symphodia.eng.domain.Order order, MappingContext context) {
                        order.setOrderType(orderDTO.orderType.toDomainType());
                    }
                }).byDefault().register();

        factory.classMap(Service.class, com.symphodia.eng.domain.item.Service.class).field("id", "productId").byDefault().register();
        factory.classMap(Card.class, com.symphodia.eng.domain.item.Card.class).field("id", "productId").byDefault().register();

    }

    public static com.symphodia.eng.domain.Order mapToDomain(Order dto) {
        return new DomainMapper().map(dto, com.symphodia.eng.domain.Order.class);
    }

}

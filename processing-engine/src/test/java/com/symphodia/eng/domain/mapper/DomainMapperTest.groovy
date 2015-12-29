package com.symphodia.eng.domain.mapper

import com.symphodia.eng.core.graph.EventProcessKey
import com.symphodia.eng.domain.OrderType
import com.symphodia.eng.domain.item.ServiceType
import com.symphodia.eng.domain.item.ProductType
import com.symphodia.eng.integration.factory.TestObjectFactory
import com.symphodia.eng.integrations.servicebus.dto.order.Card
import com.symphodia.eng.integrations.servicebus.dto.order.Service
import spock.lang.Specification

class DomainMapperTest extends Specification {

    def "should map dto to domain object"() {
        when:
        def dtoOrder = TestObjectFactory.createNewDtoOrder();
        def order = DomainMapper.mapToDomain(dtoOrder);

        then:
        order.id == dtoOrder.orderId
        order.orderType == OrderType.REGULAR
        order.getProcessKey() == new EventProcessKey(OrderType.REGULAR, ServiceType.NEW)
        order.customer.firstName == dtoOrder.customer.firstName
        order.customer.lastName == dtoOrder.customer.lastName
        order.customer.language == dtoOrder.customer.language
        order.customer.deliveryAddress.city == dtoOrder.customer.deliveryAddress.city
        order.customer.deliveryAddress.street == dtoOrder.customer.deliveryAddress.street
        order.customer.deliveryAddress.country == dtoOrder.customer.deliveryAddress.country
        order.customer.deliveryAddress.zip == dtoOrder.customer.deliveryAddress.zip
        order.orderItems.size() == dtoOrder.products.size()
        order.getOrderItem(Service.class).productId == dtoOrder.getProduct(Service.class).id
        order.getOrderItem(Service.class).domain == dtoOrder.getProduct(Service.class).domain
        order.getOrderItem(Service.class).productType == ProductType.SERVICE
        order.getOrderItem(Service.class).serviceType == ServiceType.NEW
        order.getOrderItem(Card.class).productId == dtoOrder.getProduct(Card.class).id
        order.getOrderItem(Card.class).productType == ProductType.CARD

    }
}
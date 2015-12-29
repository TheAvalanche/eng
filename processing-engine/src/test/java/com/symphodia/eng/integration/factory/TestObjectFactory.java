package com.symphodia.eng.integration.factory;

import com.symphodia.eng.domain.item.OrderItem;
import com.symphodia.eng.domain.item.Service;
import com.symphodia.eng.integrations.servicebus.dto.order.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestObjectFactory {

    public static Order createNewDtoOrder() {
        Order order = new Order();
        order.orderId = "M000001";
        order.orderType = OrderType.REGULAR;
        order.customer = createDtoCustomer();
        order.products = Arrays.asList(createDtoService(), createDtoCard());
        return order;
    }

    public static Customer createDtoCustomer() {
        Customer customer = new Customer();
        customer.firstName = "Wolfgan";
        customer.lastName = "Mozart";
        customer.language = "at";
        customer.deliveryAddress = createDtoAddress();
        return customer;
    }

    public static com.symphodia.eng.integrations.servicebus.dto.order.Address createDtoAddress() {
        com.symphodia.eng.integrations.servicebus.dto.order.Address address = new com.symphodia.eng.integrations.servicebus.dto.order.Address();
        address.street = "Mozart";
        address.country = "at";
        address.city = "Viena";
        address.zip = "AT0000";
        return address;
    }

    public static com.symphodia.eng.integrations.servicebus.dto.order.Service createDtoService() {
        com.symphodia.eng.integrations.servicebus.dto.order.Service service = new com.symphodia.eng.integrations.servicebus.dto.order.Service();
        service.id = "SUB01";
        service.serviceType = ServiceType.NEW;
        service.domain = "at";
        service.productType = ProductType.SERVICE;
        return service;
    }

    public static Card createDtoCard() {
        Card sim = new Card();
        sim.id = "SIM01";
        sim.productType = ProductType.CARD;
        return sim;
    }

    public static com.symphodia.eng.domain.Order createOrder() {
        com.symphodia.eng.domain.Order order = new com.symphodia.eng.domain.Order();
        order.setOrderId("M123456");
        com.symphodia.eng.domain.Customer customer = new com.symphodia.eng.domain.Customer();
        customer.setFirstName("Oleg Ivanov");
        com.symphodia.eng.domain.Address deliveryAddress = new com.symphodia.eng.domain.Address();
        customer.setDeliveryAddress(deliveryAddress);
        order.setCustomer(customer);
        List<OrderItem> orderItems = new ArrayList<>();
        Service service = new Service();
        orderItems.add(service);
        com.symphodia.eng.domain.item.Card card = new com.symphodia.eng.domain.item.Card();
        orderItems.add(card);
        order.setOrderItems(orderItems);
        return order;
    }
}

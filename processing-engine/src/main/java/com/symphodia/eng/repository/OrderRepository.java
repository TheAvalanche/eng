package com.symphodia.eng.repository;

import com.symphodia.eng.domain.Order;
import org.springframework.data.repository.Repository;

public interface OrderRepository extends Repository<Order, String> {

    void save(Order order);

    Order findOne(String orderId);

    Order findByCorrelationId(String correlationId);

}

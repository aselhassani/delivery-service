package com.delivery.port.repository;

import com.delivery.domain.model.Order;

import java.util.Optional;

public interface OrderRepository {

    Optional<Order> findById(String orderId);
    Order save(Order order);
}
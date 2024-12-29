package com.delivery.domain.service;

import com.delivery.adapter.repository.OrderMongoRepository;
import com.delivery.domain.model.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {

  private final OrderMongoRepository repository;

  public Optional<Order> findById(String orderId) {
    return repository.findById(orderId);
  }
}

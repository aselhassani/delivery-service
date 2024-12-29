package com.delivery.adapter.repository;

import com.delivery.adapter.repository.document.OrderDocument;
import com.delivery.adapter.repository.document.OrderDocumentMongoRepository;
import com.delivery.domain.model.Order;
import com.delivery.port.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class OrderMongoRepository implements OrderRepository {

  private final OrderDocumentMongoRepository orderDocumentMongoRepository;

  @Override
  public Optional<Order> findById(String orderId) {
    return orderDocumentMongoRepository.findById(orderId)
      .map(OrderDocument::toDomain);
  }

  @Override
  public Order save(Order order) {
    return Optional.of(order)
      .map(OrderDocument::fromDomain)
      .map(orderDocumentMongoRepository::save)
      .map(OrderDocument::toDomain)
      .orElse(null);
  }

}

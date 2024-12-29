package com.delivery.domain.service;

import com.delivery.adapter.repository.DeliverySlotMongoRepository;
import com.delivery.adapter.repository.OrderMongoRepository;
import com.delivery.domain.model.Delivery;
import com.delivery.domain.model.DeliverySlot;
import com.delivery.domain.model.Order;
import com.delivery.port.publisher.DeliveryEventPublisher;
import com.delivery.port.repository.DeliveryRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeliveryService {

  private final DeliverySlotMongoRepository deliverySlotMongoRepository;
  private final OrderMongoRepository orderRepository;
  private final DeliveryRepository deliveryRepository;
  private final DeliveryEventPublisher publisher;

  public Optional<Delivery> reserveSlot(Order order, DeliverySlot slot) {

    return Optional.of(slot)
      .map(DeliverySlot::markAsReserved)
      .map(deliverySlotMongoRepository::save)
      .flatMap(savedSlot ->
        Optional.of(order)
          .map(Order::markDeliveryStatusAsScheduled)
          .map(orderRepository::save)
          .map(savedOrder -> Delivery.fromOrderAndSlot(savedOrder, savedSlot))
      )
      .map(delivery -> {
        var savedDelivery = deliveryRepository.save(delivery);
        publisher.publishDeliveryEvent(savedDelivery);
        return savedDelivery;
      });
  }
}

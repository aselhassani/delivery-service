package com.delivery.adapter.repository;

import com.delivery.adapter.repository.document.DeliverySlotDocument;
import com.delivery.adapter.repository.document.DeliverySlotDocumentMongoRepository;
import com.delivery.domain.model.DeliveryMode;
import com.delivery.domain.model.DeliverySlot;
import com.delivery.port.repository.DeliverySlotRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class DeliverySlotMongoRepository implements DeliverySlotRepository {

  private final DeliverySlotDocumentMongoRepository deliverySlotMongoRepository;

  @Override
  public List<DeliverySlot> getAvailableDeliverySlots(DeliveryMode deliveryMode) {
    return deliverySlotMongoRepository.findByReservedFalse()
      .stream()
      .map(DeliverySlotDocument::toDomain)
      .toList();
  }

  @Override
  public Optional<DeliverySlot> findById(String orderId) {
    return deliverySlotMongoRepository.findById(orderId)
      .map(DeliverySlotDocument::toDomain);
  }

  @Override
  public DeliverySlot save(DeliverySlot slot) {
    return Optional.of(slot)
      .map(DeliverySlotDocument::fromDomain)
      .map(deliverySlotMongoRepository::save)
      .map(DeliverySlotDocument::toDomain)
      .orElse(null);
  }
}

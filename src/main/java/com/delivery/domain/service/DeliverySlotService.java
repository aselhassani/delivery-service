package com.delivery.domain.service;

import com.delivery.adapter.repository.DeliverySlotMongoRepository;
import com.delivery.domain.model.DeliveryMode;
import com.delivery.domain.model.DeliverySlot;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DeliverySlotService {

  private final DeliverySlotMongoRepository repository;

  public List<DeliverySlot> getAvailableDeliverySlots(DeliveryMode deliveryMode) {
    return repository.getAvailableDeliverySlots(deliveryMode);
  }

  public Optional<DeliverySlot> findById(String slotId) {
    return repository.findById(slotId);
  }
}

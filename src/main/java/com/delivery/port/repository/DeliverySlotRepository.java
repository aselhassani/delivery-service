package com.delivery.port.repository;

import com.delivery.domain.model.DeliveryMode;
import com.delivery.domain.model.DeliverySlot;

import java.util.List;
import java.util.Optional;

public interface DeliverySlotRepository {
    List<DeliverySlot> getAvailableDeliverySlots(DeliveryMode deliveryMode);
    Optional<DeliverySlot> findById(String orderId);
    DeliverySlot save(DeliverySlot slot);
}

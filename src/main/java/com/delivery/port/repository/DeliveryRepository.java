package com.delivery.port.repository;

import com.delivery.domain.model.Delivery;

public interface DeliveryRepository {
    Delivery save(Delivery delivery);
}
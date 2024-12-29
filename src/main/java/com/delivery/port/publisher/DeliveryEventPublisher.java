package com.delivery.port.publisher;

import com.delivery.domain.model.Delivery;

public interface DeliveryEventPublisher {
  void publishDeliveryEvent(Delivery delivery);
}

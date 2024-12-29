package com.delivery.domain.model;

import lombok.Builder;
import lombok.With;

@Builder
public record Order(
  String orderId,
  @With
  DeliveryStatus deliveryStatus
) {

  public Order markDeliveryStatusAsScheduled() {
    return withDeliveryStatus(DeliveryStatus.SCHEDULED);
  }
}

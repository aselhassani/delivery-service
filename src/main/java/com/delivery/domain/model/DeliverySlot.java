package com.delivery.domain.model;

import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.With;

@Builder
public record DeliverySlot(
  String slotId,
  DeliveryMode deliveryMode,
  OffsetDateTime start,
  OffsetDateTime end,
  @With
  boolean reserved
) {

  public DeliverySlot markAsReserved() {
    return withReserved(true);
  }

}

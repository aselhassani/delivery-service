package com.delivery.adapter.ws.dto;

import com.delivery.domain.model.DeliverySlot;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record DeliverySlotDto(
  String slotId,
  DeliveryMode deliveryMode,
  OffsetDateTime start,
  OffsetDateTime end

) {

  public static DeliverySlotDto fromDomain(DeliverySlot domain) {
    return DeliverySlotDto.builder()
      .slotId(domain.slotId())
      .deliveryMode(DeliveryMode.valueOf(domain.deliveryMode().name()))
      .start(domain.start())
      .end(domain.end())
      .build();
  }
}



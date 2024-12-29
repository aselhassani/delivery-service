package com.delivery.adapter.ws.dto;

import com.delivery.domain.model.Delivery;
import lombok.Builder;

@Builder
public record DeliveryDto(
  String deliveryId,
  String slotId,
  String orderId,
  DeliveryMode deliveryMode,
  DeliveryStatus deliveryStatus
) {

  public static DeliveryDto fromDomain(Delivery domain) {
    return DeliveryDto.builder()
      .deliveryId(domain.deliveryId())
      .orderId(domain.orderId())
      .slotId(domain.slotId())
      .deliveryMode(DeliveryMode.valueOf(domain.deliveryMode().name()))
      .deliveryStatus(DeliveryStatus.valueOf(domain.deliveryStatus().name()))
      .build();
  }

}

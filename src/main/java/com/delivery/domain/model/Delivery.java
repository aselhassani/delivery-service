package com.delivery.domain.model;

import lombok.Builder;

@Builder
public record Delivery(
  String deliveryId,
  String slotId,
  String orderId,
  DeliveryMode deliveryMode,
  DeliveryStatus deliveryStatus

) {

  public static Delivery fromOrderAndSlot(Order order, DeliverySlot slot) {

    return Delivery.builder()
      .slotId(slot.slotId())
      .orderId(order.orderId())
      .deliveryStatus(order.deliveryStatus())
      .deliveryMode(slot.deliveryMode())
      .build();
  }



}

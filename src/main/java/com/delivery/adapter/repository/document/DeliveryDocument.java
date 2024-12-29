package com.delivery.adapter.repository.document;

import com.delivery.domain.model.Delivery;
import com.delivery.domain.model.DeliveryMode;
import com.delivery.domain.model.DeliveryStatus;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Builder
@Document
public class DeliveryDocument {
  @Id
  @MongoId
  private String deliveryId;
  private String slotId;
  private String orderId;
  private DeliveryModeDocument deliveryMode;
  private DeliveryStatusDocument deliveryStatus;

  public Delivery toDomain() {
    return Delivery.builder()
      .deliveryId(deliveryId)
      .slotId(slotId)
      .orderId(orderId)
      .deliveryMode(DeliveryMode.valueOf(deliveryMode.name()))
      .deliveryStatus(DeliveryStatus.valueOf(deliveryStatus.name()))
      .build();
  }

  public static DeliveryDocument fromDomain(Delivery domain) {
    return DeliveryDocument.builder()
      .deliveryId(domain.deliveryId())
      .slotId(domain.slotId())
      .orderId(domain.orderId())
      .deliveryMode(DeliveryModeDocument.valueOf(domain.deliveryMode().name()))
      .deliveryStatus(DeliveryStatusDocument.valueOf(domain.deliveryStatus().name()))
      .build();
  }
}

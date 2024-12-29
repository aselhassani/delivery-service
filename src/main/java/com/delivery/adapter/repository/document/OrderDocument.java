package com.delivery.adapter.repository.document;

import com.delivery.domain.model.DeliveryStatus;
import com.delivery.domain.model.Order;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Builder
@Document
@Getter
public class OrderDocument {

  @Id
  @MongoId
  private String orderId;
  private DeliveryStatusDocument deliveryStatus;

  public Order toDomain() {
    return Order.builder()
      .orderId(orderId)
      .deliveryStatus(DeliveryStatus.valueOf(deliveryStatus.name()))
      .build();
  }

  public static OrderDocument fromDomain(Order domain) {
    return OrderDocument.builder()
      .orderId(domain.orderId())
      .deliveryStatus(DeliveryStatusDocument.valueOf(domain.deliveryStatus().name()))
      .build();
  }
}

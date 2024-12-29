package com.delivery.adapter.repository.document;

import com.delivery.domain.model.DeliveryMode;
import com.delivery.domain.model.DeliverySlot;
import java.time.Instant;
import java.time.ZoneOffset;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@Builder
@Getter
public class DeliverySlotDocument {

  @Id
  @MongoId
  private String slotId;
  @Indexed(name = "delivery_slot_delivery_mode_idx")
  private DeliveryModeDocument deliveryMode;
  private Instant start;
  private Instant end;
  private boolean reserved;

  public DeliverySlot toDomain() {
    return DeliverySlot.builder()
      .slotId(slotId)
      .start(start.atOffset(ZoneOffset.UTC))
      .end(end.atOffset(ZoneOffset.UTC))
      .deliveryMode(DeliveryMode.valueOf(deliveryMode.name()))
      .reserved(reserved)
      .build();
  }

  public static DeliverySlotDocument fromDomain(DeliverySlot domain) {
    return DeliverySlotDocument.builder()
      .slotId(domain.slotId())
      .start(domain.start().toInstant())
      .end(domain.end().toInstant())
      .deliveryMode(DeliveryModeDocument.valueOf(domain.deliveryMode().name()))
      .reserved(domain.reserved())
      .build();
  }
}

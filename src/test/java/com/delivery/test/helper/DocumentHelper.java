package com.delivery.test.helper;

import com.delivery.adapter.repository.document.DeliveryDocument;
import com.delivery.adapter.repository.document.DeliveryModeDocument;
import com.delivery.adapter.repository.document.DeliverySlotDocument;
import com.delivery.adapter.repository.document.DeliveryStatusDocument;
import com.delivery.adapter.repository.document.OrderDocument;
import java.time.OffsetDateTime;
import java.util.Random;

public class DocumentHelper {

  public static OrderDocument getRandomOrderDocument() {
    return getRandomOrderDocument(TestHelper.getRandom(DeliveryStatusDocument.class));
  }

  public static OrderDocument getRandomOrderDocument(DeliveryStatusDocument deliveryStatus) {
    return OrderDocument.builder()
        .orderId(TestHelper.getRandomId())
        .deliveryStatus(deliveryStatus)
        .build();
  }

  public static DeliverySlotDocument getRandomDeliverySlotDocument(boolean reserved) {

    var start = OffsetDateTime.now();
    var end = start.plusHours(2);

    return DeliverySlotDocument.builder()
        .slotId(TestHelper.getRandomId())
        .deliveryMode(TestHelper.getRandom(DeliveryModeDocument.class))
        .start(start.toInstant())
        .end(end.toInstant())
        .reserved(reserved)
        .build();
  }

  public static DeliverySlotDocument getRandomDeliverySlotDocument() {
    return getRandomDeliverySlotDocument(new Random().nextBoolean());
  }

  public static DeliveryDocument getRandomDeliveryDocument() {

    return DeliveryDocument.builder()
      .deliveryId(TestHelper.getRandomId())
      .slotId(TestHelper.getRandomId())
      .orderId(TestHelper.getRandomId())
      .deliveryStatus(TestHelper.getRandom(DeliveryStatusDocument.class))
      .deliveryMode(TestHelper.getRandom(DeliveryModeDocument.class))
      .build();
  }
}

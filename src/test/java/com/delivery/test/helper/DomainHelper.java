package com.delivery.test.helper;

import com.delivery.domain.model.Delivery;
import com.delivery.domain.model.DeliveryMode;
import com.delivery.domain.model.DeliverySlot;
import com.delivery.domain.model.DeliveryStatus;
import com.delivery.domain.model.Order;

import java.time.OffsetDateTime;
import java.util.Random;

public class DomainHelper {

  private static final Random random = new Random();

  public static DeliverySlot getRandomDeliverySlot() {

    return getRandomDeliverySlot(new Random().nextBoolean());
  }

  public static DeliverySlot getRandomDeliverySlot(boolean reserved) {

    var start = OffsetDateTime.now();
    var end = start.plusHours(2);

    return DeliverySlot.builder()
      .deliveryMode(TestHelper.getRandom(DeliveryMode.class))
      .start(start)
      .end(end)
      .reserved(reserved)
      .build();
  }


  public static Order getRandomOrder(DeliveryStatus deliveryStatus) {
    return Order.builder()
      .orderId(TestHelper.getRandomId())
      .deliveryStatus(deliveryStatus)
      .build();

  }

  public static Delivery getRandomDelivery(DeliveryStatus deliveryStatus) {
    return Delivery.builder()
      .deliveryId(TestHelper.getRandomId())
      .deliveryMode(TestHelper.getRandom(DeliveryMode.class))
      .deliveryStatus(deliveryStatus)
      .orderId(TestHelper.getRandomId())
      .slotId(TestHelper.getRandomId())
      .build();
  }
}

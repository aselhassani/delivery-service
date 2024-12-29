package com.delivery.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.delivery.adapter.repository.DeliveryMongoRepository;
import com.delivery.adapter.repository.DeliverySlotMongoRepository;
import com.delivery.adapter.repository.OrderMongoRepository;
import com.delivery.domain.model.Delivery;
import com.delivery.domain.model.DeliverySlot;
import com.delivery.domain.model.DeliveryStatus;
import com.delivery.domain.model.Order;
import com.delivery.port.publisher.DeliveryEventPublisher;
import com.delivery.test.helper.DomainHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceTest {

  @InjectMocks
  private DeliveryService underTest;
  @Mock
  private DeliverySlotMongoRepository deliverySlotMongoRepository;
  @Mock
  private OrderMongoRepository orderRepository;
  @Mock
  private DeliveryMongoRepository deliveryMongoRepository;
  @Mock
  private DeliveryEventPublisher deliveryEventPublisher;

  @Test
  void reserveSlotShouldReserveSlotScheduleOrderDeliveryAndPublishEvent() {

    var slot = DomainHelper.getRandomDeliverySlot(false);
    var order = DomainHelper.getRandomOrder(DeliveryStatus.PENDING);

    var savedSlot = DomainHelper.getRandomDeliverySlot(true);
    var savedOrder = DomainHelper.getRandomOrder(DeliveryStatus.SCHEDULED);
    var delivery = DomainHelper.getRandomDelivery(DeliveryStatus.SCHEDULED);

    when(deliverySlotMongoRepository.save(any())).thenReturn(savedSlot);
    when(orderRepository.save(any())).thenReturn(savedOrder);
    when(deliveryMongoRepository.save(any())).thenReturn(delivery);

    var result = underTest.reserveSlot(order, slot);

    assertSlotReserved(slot);
    assertOrderDeliveryScheduled(order);
    assertDeliveryCreatedFromOrderAndSlot(savedOrder, savedSlot);
    assertSlotReservedEventPublished(delivery);
    assertThat(result).contains(delivery);
  }

  private void assertSlotReservedEventPublished(Delivery delivery) {
    verify(deliveryEventPublisher).publishDeliveryEvent(delivery);
  }

  private void assertDeliveryCreatedFromOrderAndSlot(Order order, DeliverySlot slot) {

    var captor = ArgumentCaptor.forClass(Delivery.class);
    verify(deliveryMongoRepository).save(captor.capture());

    var delivery = captor.getValue();

    assertThat(delivery.slotId()).isEqualTo(slot.slotId());
    assertThat(delivery.deliveryMode()).isEqualTo(slot.deliveryMode());
    assertThat(delivery.orderId()).isEqualTo(order.orderId());
    assertThat(delivery.deliveryStatus()).isEqualTo(order.deliveryStatus());

  }

  private void assertSlotReserved(DeliverySlot slot) {

    var captor = ArgumentCaptor.forClass(DeliverySlot.class);
    verify(deliverySlotMongoRepository).save(captor.capture());

    var savedSlot = captor.getValue();

    assertThat(savedSlot)
      .usingRecursiveComparison()
      .ignoringFields("reserved")
      .isEqualTo(slot);

    assertThat(savedSlot.reserved())
      .isTrue();
  }

  private void assertOrderDeliveryScheduled(Order order) {

    var captor = ArgumentCaptor.forClass(Order.class);
    verify(orderRepository).save(captor.capture());
    var capturedOrder = captor.getValue();

    assertThat(capturedOrder)
      .usingRecursiveComparison()
      .ignoringFields("deliveryStatus")
      .isEqualTo(order);

    assertThat(capturedOrder.deliveryStatus())
      .isEqualTo(DeliveryStatus.SCHEDULED);
  }
}

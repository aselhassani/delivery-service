package com.delivery.domain.service;

import com.delivery.adapter.repository.DeliverySlotMongoRepository;
import com.delivery.domain.model.DeliveryMode;
import com.delivery.test.helper.DomainHelper;
import com.delivery.test.helper.TestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliverySlotServiceTest {
  @InjectMocks
  private DeliverySlotService underTest;
  @Mock
  private DeliverySlotMongoRepository repository;

  @Test
  void findByIdRetrievesDeliverySlot() {

    var slotId = TestHelper.getRandomId();
    var deliverySlot = DomainHelper.getRandomDeliverySlot();

    when(repository.findById(any())).thenReturn(Optional.of(deliverySlot));

    var result = underTest.findById(slotId);

    verify(repository).findById(slotId);
    assertThat(result).contains(deliverySlot);

  }

  @Test
  void getAvailableDeliverySlots() {
    var deliverySlot = DomainHelper.getRandomDeliverySlot();
    var deliveryMode = TestHelper.getRandom(DeliveryMode.class);

    when(repository.getAvailableDeliverySlots(any())).thenReturn(List.of(deliverySlot));

    var result = underTest.getAvailableDeliverySlots(deliveryMode);

    verify(repository).getAvailableDeliverySlots(deliveryMode);
    assertThat(result).containsExactly(deliverySlot);
  }



}

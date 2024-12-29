package com.delivery.adapter.ws;

import com.delivery.adapter.ws.dto.DeliveryMode;
import com.delivery.domain.service.DeliverySlotService;
import com.delivery.test.helper.DomainHelper;
import com.delivery.test.helper.TestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliverySlotControllerTest {

  @InjectMocks
  private DeliverySlotController underTest;

  @Mock
  private DeliverySlotService deliverySlotService;


  @Test
  void getAvailableSlotRetrievesAvailableSlots() {

    var deliveryMode = TestHelper.getRandom(DeliveryMode.class);
    var deliverySlot = DomainHelper.getRandomDeliverySlot(false);

    when(deliverySlotService.getAvailableDeliverySlots(any())).thenReturn(List.of(deliverySlot));

    var result = underTest.getAvailableDeliverySlots(deliveryMode);

    verify(deliverySlotService).getAvailableDeliverySlots(com.delivery.domain.model.DeliveryMode.valueOf(deliveryMode.name()));

    assertThat(result)
      .isNotNull()
      .satisfies(res -> {
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody())
          .usingRecursiveFieldByFieldElementComparatorIgnoringFields("reserved")
          .isEqualTo(List.of(deliverySlot));
      });


  }

}

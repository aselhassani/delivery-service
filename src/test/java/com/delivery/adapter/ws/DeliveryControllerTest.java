package com.delivery.adapter.ws;

import static java.util.function.Predicate.not;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.delivery.adapter.ws.dto.ReserveSlotDto;
import com.delivery.adapter.ws.exception.DeliverySlotNotFoundException;
import com.delivery.adapter.ws.exception.InvalidOrderDeliveryStatusForSlotResaException;
import com.delivery.adapter.ws.exception.OrderNotFoundException;
import com.delivery.adapter.ws.exception.SlotAlreadyReservedException;
import com.delivery.domain.model.DeliveryStatus;
import com.delivery.domain.service.DeliveryService;
import com.delivery.domain.service.DeliverySlotService;
import com.delivery.domain.service.OrderService;
import com.delivery.test.helper.DomainHelper;
import com.delivery.test.helper.DtoHelper;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class DeliveryControllerTest {
  @InjectMocks
  private DeliveryController underTest;
  @Mock
  private OrderService orderService;
  @Mock
  private DeliverySlotService deliverySlotService;
  @Mock
  private DeliveryService deliveryService;

  private ReserveSlotDto reserveSlotDto;

  @BeforeEach
  void setup() {
    reserveSlotDto = DtoHelper.generateRandomReserveSlotDto();
  }

  @Test
  void reserveSlotThrowsExceptionForNonExistentOrder() {



    when(orderService.findById(any())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> underTest.reserveSlot(reserveSlotDto))
      .isInstanceOf(OrderNotFoundException.class)
      .hasMessage("No order with id '"+reserveSlotDto.orderId()+"' was found");

  }

  @ParameterizedTest
  @MethodSource("provideInvalidOrderDeliveryStatusesForSlotResa")
  void reserveSlotThrowsExceptionForInvalidOrderDeliveryStatus(String deliveryStatus) {

    when(orderService.findById(any())).thenReturn(Optional.of(DomainHelper.getRandomOrder(DeliveryStatus.valueOf(deliveryStatus))));

    assertThatThrownBy(() -> underTest.reserveSlot(reserveSlotDto))
      .isInstanceOf(InvalidOrderDeliveryStatusForSlotResaException.class)
      .hasMessage("Slot reservation failed: Order delivery status must be 'PENDING' but was '" + deliveryStatus+"'");
  }

  @Test
  void reserveSlotThrowsExceptionForNonExistentSlot() {

    when(orderService.findById(any())).thenReturn(Optional.of(DomainHelper.getRandomOrder(DeliveryStatus.PENDING)));
    when(deliverySlotService.findById(any())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> underTest.reserveSlot(reserveSlotDto))
      .isInstanceOf(DeliverySlotNotFoundException.class)
      .hasMessage("No slot with id '"+reserveSlotDto.slotId()+"' was found");
  }

  @Test
  void reserveSlotThrowsExceptionForAlreadyReservedSlot() {

    var reservedDeliverySlot = DomainHelper.getRandomDeliverySlot(true);

    when(orderService.findById(any())).thenReturn(Optional.of(DomainHelper.getRandomOrder(DeliveryStatus.PENDING)));
    when(deliverySlotService.findById(any())).thenReturn(Optional.of(reservedDeliverySlot));

    assertThatThrownBy(() -> underTest.reserveSlot(reserveSlotDto))
      .isInstanceOf(SlotAlreadyReservedException.class)
      .hasMessage("Slot reservation failed: Delivery slot '"+reserveSlotDto.slotId()+ "' is already reserved");
  }

  @Test
  void reserveSlotReservesSlotAndReturnDeliveryDto() {

    var deliverySlot = DomainHelper.getRandomDeliverySlot(false);
    var order = DomainHelper.getRandomOrder(DeliveryStatus.PENDING);
    var delivery = DomainHelper.getRandomDelivery(DeliveryStatus.SCHEDULED);


    when(orderService.findById(any())).thenReturn(Optional.of(order));
    when(deliverySlotService.findById(any())).thenReturn(Optional.of(deliverySlot));
    when(deliveryService.reserveSlot(any(), any())).thenReturn(Optional.of(delivery));

    var result = underTest.reserveSlot(reserveSlotDto);

    verify(deliveryService).reserveSlot(order, deliverySlot);

    assertThat(result)
      .isNotNull()
      .satisfies(res -> {
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody())
          .usingRecursiveComparison()
          .isEqualTo(delivery);
      });

  }

  private static Stream<String> provideInvalidOrderDeliveryStatusesForSlotResa() {
    return Arrays.stream(DeliveryStatus.values())
      .filter(not(DeliveryStatus.PENDING::equals))
      .map(Enum::name);
  }

}

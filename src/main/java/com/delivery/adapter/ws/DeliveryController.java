package com.delivery.adapter.ws;

import static java.util.function.Function.identity;

import com.delivery.adapter.ws.dto.DeliveryDto;
import com.delivery.adapter.ws.dto.ErrorDto;
import com.delivery.adapter.ws.dto.ReserveSlotDto;
import com.delivery.adapter.ws.exception.DeliverySlotNotFoundException;
import com.delivery.adapter.ws.exception.InvalidOrderDeliveryStatusForSlotResaException;
import com.delivery.adapter.ws.exception.OrderNotFoundException;
import com.delivery.adapter.ws.exception.SlotAlreadyReservedException;
import com.delivery.domain.model.DeliveryStatus;
import com.delivery.domain.service.DeliveryService;
import com.delivery.domain.service.DeliverySlotService;
import com.delivery.domain.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/delivery")
@AllArgsConstructor
@Tag(name = "Delivery", description = "Operations related to delivery")
public class DeliveryController {

  private final DeliveryService deliveryService;
  private final OrderService orderService;
  private final DeliverySlotService deliverySlotService;

  @Operation(
      summary = "Reserve a delivery slot for an order",
      description = "This endpoint allows reserving a delivery slot for a given order.",
      tags = {"Delivery"}
  )
  @ApiResponses(value = {
  @ApiResponse(responseCode = "200", description = "Slot successfully reserved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeliveryDto.class))),
  @ApiResponse(responseCode = "400", description = "Invalid order status or slot already reserved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))),
  @ApiResponse(responseCode = "404", description = "Order or delivery slot not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))),
  @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping
  public ResponseEntity<DeliveryDto> reserveSlot(@RequestBody ReserveSlotDto dto) {

    var orderId = dto.orderId();
    var slotId = dto.slotId();

    var order = orderService.findById(orderId)
      .orElseThrow(() -> new OrderNotFoundException(orderId));

    if (!DeliveryStatus.PENDING.equals(order.deliveryStatus())) {
      throw new InvalidOrderDeliveryStatusForSlotResaException(order.deliveryStatus());
    }

    var deliverySlot = deliverySlotService.findById(slotId)
      .orElseThrow(() -> new DeliverySlotNotFoundException(slotId));

    if (deliverySlot.reserved()) {
      throw new SlotAlreadyReservedException(slotId);
    }

   return Optional.of(deliveryService.reserveSlot(order, deliverySlot))
      .flatMap(identity())
      .map(DeliveryDto::fromDomain)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.noContent().build());
  }

}

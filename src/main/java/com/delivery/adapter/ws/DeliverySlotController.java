package com.delivery.adapter.ws;

import com.delivery.adapter.ws.dto.DeliverySlotDto;
import com.delivery.domain.model.DeliveryMode;
import com.delivery.domain.service.DeliverySlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Delivery Slots", description = "Operations related to available delivery slots")
@RestController
@RequestMapping("/slots")
@AllArgsConstructor
public class DeliverySlotController {

  private final DeliverySlotService deliverySlotService;

  @Operation(
      summary = "Get available delivery slots by delivery mode",
      description = "This endpoint retrieves all available delivery slots for the specified delivery mode (e.g., DELIVERY_ASAP, DELIVERY, DRIVE).",
      tags = {"Delivery Slots"}
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "List of available delivery slots retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeliverySlotDto.class))),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/{deliveryMode}")
  public ResponseEntity<List<DeliverySlotDto>> getAvailableDeliverySlots(
      @PathVariable(value = "deliveryMode")
      @Parameter(description = "The delivery mode for which to retrieve available slots")
      com.delivery.adapter.ws.dto.DeliveryMode deliveryMode) {

    var availableDeliverySlots = deliverySlotService.getAvailableDeliverySlots(DeliveryMode.valueOf(deliveryMode.name()))
        .stream()
        .map(DeliverySlotDto::fromDomain)
        .toList();

    return ResponseEntity.ok(availableDeliverySlots);
  }
}


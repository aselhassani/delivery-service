package com.delivery.adapter.ws;

import com.delivery.domain.model.DeliveryMode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Delivery Modes", description = "Operations related to delivery modes")
@RestController
@RequestMapping("/delivery-modes")
public class DeliveryModeController {

  @Operation(
      summary = "Get list of available delivery modes",
      description = "This endpoint returns a list of all available delivery modes (e.g., DELIVERY_ASAP, DRIVE, DELIVERY).",
      tags = {"Delivery Modes"}
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "List of delivery modes successfully retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class, type = "array"))),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping
  public ResponseEntity<List<String>> getDeliveryModes() {

    var deliveryModes = Arrays.stream(DeliveryMode.values())
        .map(DeliveryMode::name)
        .toList();

    return ResponseEntity.ok(deliveryModes);
  }
}

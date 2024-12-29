package com.delivery.adapter.ws.exception;

import com.delivery.adapter.ws.dto.ErrorCode;
import com.delivery.domain.model.DeliveryStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidOrderDeliveryStatusForSlotResaException extends ApiException {

  private final DeliveryStatus deliveryStatus;

  public InvalidOrderDeliveryStatusForSlotResaException(DeliveryStatus status) {
    super(ErrorCode.INVALID_ORDER_DELIVERY_STATUS, HttpStatus.BAD_REQUEST);
    this.deliveryStatus = status;
  }

  @Override public String getMessage() {
    return String.format("Slot reservation failed: Order delivery status must be 'PENDING' but was '%s'", deliveryStatus);
  }
}

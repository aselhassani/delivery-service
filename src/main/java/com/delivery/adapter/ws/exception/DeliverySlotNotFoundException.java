package com.delivery.adapter.ws.exception;

import com.delivery.adapter.ws.dto.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DeliverySlotNotFoundException extends ApiException {

  private final String slotId;

  public DeliverySlotNotFoundException(String slotId) {
    super(ErrorCode.DELIVERY_SLOT_NOT_FOUND, HttpStatus.NOT_FOUND);
    this.slotId = slotId;
  }

  @Override
  public String getMessage() {
    return String.format("No slot with id '%s' was found", slotId);
  }
}

package com.delivery.adapter.ws.exception;

import com.delivery.adapter.ws.dto.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SlotAlreadyReservedException extends ApiException {

  private final String slotId;

  public SlotAlreadyReservedException(String slotId){
    super(ErrorCode.DELIVERY_SLOT_NOT_AVAILABLE, HttpStatus.BAD_REQUEST);
    this.slotId = slotId;
  }

  @Override public String getMessage() {
    return String.format("Slot reservation failed: Delivery slot '%s' is already reserved", slotId);
  }
}

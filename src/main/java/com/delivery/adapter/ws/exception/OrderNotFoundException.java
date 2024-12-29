package com.delivery.adapter.ws.exception;

import com.delivery.adapter.ws.dto.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class OrderNotFoundException extends ApiException {

  private final String orderId;

  public OrderNotFoundException(String orderId) {
    super(ErrorCode.ORDER_NOT_FOUND, HttpStatus.NOT_FOUND);
    this.orderId = orderId;
  }

  @Override
  public String getMessage() {
    return String.format("No order with id '%s' was found", orderId);
  }
}

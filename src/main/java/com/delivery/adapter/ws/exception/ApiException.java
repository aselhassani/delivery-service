package com.delivery.adapter.ws.exception;

import com.delivery.adapter.ws.dto.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public abstract class ApiException extends RuntimeException {

  private final ErrorCode errorCode;
  private final HttpStatus httpStatus;

}

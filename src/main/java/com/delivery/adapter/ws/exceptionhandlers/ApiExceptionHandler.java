package com.delivery.adapter.ws.exceptionhandlers;

import com.delivery.adapter.ws.dto.ErrorCode;
import com.delivery.adapter.ws.dto.ErrorDto;
import com.delivery.adapter.ws.exception.ApiException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Log4j2
public class ApiExceptionHandler {

  @ExceptionHandler(value = ApiException.class)
  public ResponseEntity<ErrorDto> handleApiException(ApiException e) {
    log.error(e);
    var status = e.getHttpStatus();
    var errorDto = new ErrorDto(e.getErrorCode().name(), e.getMessage());
    return new ResponseEntity<>(errorDto, status);
  }

  @ExceptionHandler(value = Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ErrorDto handleAll(Exception e) {
    log.error(e);
    return new ErrorDto(ErrorCode.INTERNAL_ERROR.name(), "An unexpected condition was encountered");
  }

}

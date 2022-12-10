package com.akmal.vodafoneorderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderPlacementException extends RuntimeException {

  public OrderPlacementException(String message) {
    super(message);
  }

  public OrderPlacementException(String message, Throwable cause) {
    super(message, cause);
  }

  public OrderPlacementException(Throwable cause) {
    super(cause);
  }

  protected OrderPlacementException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}

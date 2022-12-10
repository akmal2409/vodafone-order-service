package com.akmal.vodafoneorderservice.exception;

/**
 * Thrown in the case when we fail to pre-load user data from external datasource
 */
public class UserInMemoryCacheInitialisationException extends RuntimeException {

  public UserInMemoryCacheInitialisationException(String message) {
    super(message);
  }

  public UserInMemoryCacheInitialisationException(String message, Throwable cause) {
    super(message, cause);
  }

  public UserInMemoryCacheInitialisationException(Throwable cause) {
    super(cause);
  }

  protected UserInMemoryCacheInitialisationException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}

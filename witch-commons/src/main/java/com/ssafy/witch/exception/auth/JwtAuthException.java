package com.ssafy.witch.exception.auth;

import com.ssafy.witch.exception.ErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JwtAuthException extends AuthenticationException {

  private final ErrorCode errorCode;

  public JwtAuthException(ErrorCode errorCode) {
    super(errorCode.getErrorMessage());
    this.errorCode = errorCode;
  }
}

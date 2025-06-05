package com.ssafy.witch.exception.auth;

import com.ssafy.witch.exception.ErrorCode;

public class InvalidAccessTokenException extends JwtAuthException {

  public InvalidAccessTokenException() {
    super(ErrorCode.INVALID_ACCESS_TOKEN);
  }
}

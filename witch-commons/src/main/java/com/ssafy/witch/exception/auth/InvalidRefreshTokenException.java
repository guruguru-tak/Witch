package com.ssafy.witch.exception.auth;

import com.ssafy.witch.exception.ErrorCode;

public class InvalidRefreshTokenException extends JwtAuthException {

  public InvalidRefreshTokenException() {
    super(ErrorCode.INVALID_REFRESH_TOKEN);
  }
}

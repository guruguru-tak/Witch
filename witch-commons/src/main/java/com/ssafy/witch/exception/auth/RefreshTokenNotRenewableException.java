package com.ssafy.witch.exception.auth;

import com.ssafy.witch.exception.ErrorCode;

public class RefreshTokenNotRenewableException extends JwtAuthException {

  public RefreshTokenNotRenewableException() {
    super(ErrorCode.REFRESH_TOKEN_NOT_UPDATABLE);
  }
}

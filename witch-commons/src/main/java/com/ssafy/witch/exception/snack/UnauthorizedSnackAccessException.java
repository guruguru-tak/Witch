package com.ssafy.witch.exception.snack;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class UnauthorizedSnackAccessException extends BusinessException {

  public UnauthorizedSnackAccessException() {
    super(ErrorCode.UNAUTHORIZED_SNACK_ACCESS);
  }
}

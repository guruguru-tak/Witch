package com.ssafy.witch.exception.snack;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class SnackViewNotAvailableException extends BusinessException {

  public SnackViewNotAvailableException() {
    super(ErrorCode.SNACK_VIEW_NOT_AVAILABLE);
  }
}

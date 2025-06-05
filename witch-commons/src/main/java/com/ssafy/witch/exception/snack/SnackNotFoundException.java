package com.ssafy.witch.exception.snack;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class SnackNotFoundException extends BusinessException {

  public SnackNotFoundException() {
    super(ErrorCode.NON_EXISTENT_SNACK);
  }
}

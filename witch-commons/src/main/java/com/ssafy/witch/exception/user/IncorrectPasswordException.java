package com.ssafy.witch.exception.user;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class IncorrectPasswordException extends BusinessException {

  public IncorrectPasswordException() {
    super(ErrorCode.INCORRECT_PASSWORD);
  }

}

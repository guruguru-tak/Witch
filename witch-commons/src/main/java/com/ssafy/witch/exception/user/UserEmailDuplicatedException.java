package com.ssafy.witch.exception.user;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class UserEmailDuplicatedException extends BusinessException {

  public UserEmailDuplicatedException() {
    super(ErrorCode.EMAIL_ALREADY_IN_USE);
  }

}

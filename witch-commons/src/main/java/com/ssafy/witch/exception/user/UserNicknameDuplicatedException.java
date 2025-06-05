package com.ssafy.witch.exception.user;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class UserNicknameDuplicatedException extends BusinessException {

  public UserNicknameDuplicatedException() {
    super(ErrorCode.NICKNAME_ALREADY_IN_USE);
  }

}

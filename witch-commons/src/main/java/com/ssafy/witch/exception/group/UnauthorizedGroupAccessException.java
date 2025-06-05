package com.ssafy.witch.exception.group;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class UnauthorizedGroupAccessException extends BusinessException {

  public UnauthorizedGroupAccessException() {
    super(ErrorCode.UNAUTHORIZED_GROUP_ACCESS);
  }
}

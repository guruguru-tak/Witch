package com.ssafy.witch.exception.group;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class GroupJoinRequestExistsException extends BusinessException {

  public GroupJoinRequestExistsException() {
    super(ErrorCode.GROUP_JOIN_REQUEST_ALREADY_EXISTS);
  }
}

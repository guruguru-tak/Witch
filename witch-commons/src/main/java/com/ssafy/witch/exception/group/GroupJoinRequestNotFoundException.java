package com.ssafy.witch.exception.group;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class GroupJoinRequestNotFoundException extends BusinessException {

  public GroupJoinRequestNotFoundException() {
    super(ErrorCode.GROUP_JOIN_REQUEST_NOT_EXISTS);
  }
}

package com.ssafy.witch.exception.group;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class GroupNotFoundException extends BusinessException {

  public GroupNotFoundException() {
    super(ErrorCode.GROUP_NOT_EXIST);
  }
}

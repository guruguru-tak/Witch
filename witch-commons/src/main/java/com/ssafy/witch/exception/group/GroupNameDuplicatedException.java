package com.ssafy.witch.exception.group;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class GroupNameDuplicatedException extends BusinessException {

  public GroupNameDuplicatedException() {
    super(ErrorCode.GROUP_NAME_ALREADY_IN_USE);
  }
}

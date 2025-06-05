package com.ssafy.witch.exception.group;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class GroupNotJoinedException extends BusinessException {
  public GroupNotJoinedException() {
    super(ErrorCode.NOT_JOINED_MEETING);
  }
}

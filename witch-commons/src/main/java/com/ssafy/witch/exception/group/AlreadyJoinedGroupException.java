package com.ssafy.witch.exception.group;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class AlreadyJoinedGroupException extends BusinessException {

  public AlreadyJoinedGroupException() {
    super(ErrorCode.ALREADY_JOINED_GROUP);
  }

}

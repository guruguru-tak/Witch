package com.ssafy.witch.exception.group;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class ExceedMaxGroupParticipants extends BusinessException {

  public ExceedMaxGroupParticipants() {
    super(ErrorCode.EXCEED_MAX_GROUP_PARTICIPANTS);
  }
}

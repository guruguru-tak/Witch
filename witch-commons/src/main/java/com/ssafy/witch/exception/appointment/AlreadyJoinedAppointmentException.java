package com.ssafy.witch.exception.appointment;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class AlreadyJoinedAppointmentException extends BusinessException {

  public AlreadyJoinedAppointmentException() {
    super(ErrorCode.ALREADY_JOIN_APPOINTMENT);
  }
}

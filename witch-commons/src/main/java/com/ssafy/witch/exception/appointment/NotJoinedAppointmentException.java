package com.ssafy.witch.exception.appointment;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class NotJoinedAppointmentException extends BusinessException {

  public NotJoinedAppointmentException() {
    super(ErrorCode.NOT_JOIN_APPOINTMENT);
  }
}

package com.ssafy.witch.exception.appointment;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class UnauthorizedAppointmentAccessException extends BusinessException {

  public UnauthorizedAppointmentAccessException() {
    super(ErrorCode.UNAUTHORIZED_APPOINTMENT_ACCESS);
  }
}

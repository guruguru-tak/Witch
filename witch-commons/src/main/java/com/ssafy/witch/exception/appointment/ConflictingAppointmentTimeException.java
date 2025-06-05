package com.ssafy.witch.exception.appointment;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class ConflictingAppointmentTimeException extends BusinessException {

  public ConflictingAppointmentTimeException() {
    super(ErrorCode.CONFLICTING_APPOINTMENT_TIME);
  }
}

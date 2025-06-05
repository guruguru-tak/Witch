package com.ssafy.witch.exception.appointment;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class AppointmentTimeInPastException extends BusinessException {

  public AppointmentTimeInPastException() {
    super(ErrorCode.APPOINTMENT_TIME_IN_PAST);
  }
}

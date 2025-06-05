package com.ssafy.witch.exception.appointment;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class AppointmentNotFoundException extends BusinessException {

  public AppointmentNotFoundException() {
    super(ErrorCode.NON_EXISTENT_APPOINTMENT);
  }
}

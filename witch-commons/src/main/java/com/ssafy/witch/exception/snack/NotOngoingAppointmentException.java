package com.ssafy.witch.exception.snack;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class NotOngoingAppointmentException extends BusinessException {

  public NotOngoingAppointmentException() {super(ErrorCode.APPOINTMENT_NOT_ONGOING);}
}

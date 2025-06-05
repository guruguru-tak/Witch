package com.ssafy.witch.validate.validator;

import com.ssafy.witch.validate.annotation.AppointmentName;

public class AppointmentNameValidator extends RegexValidator<AppointmentName, String> {

  @Override
  protected String getRegex() {
    return ValidationRule.APPOINTMENT_NAME.getRegex();
  }

  @Override
  protected String getErrorMessage() {
    return ValidationRule.APPOINTMENT_NAME.getErrorMessage();
  }
}

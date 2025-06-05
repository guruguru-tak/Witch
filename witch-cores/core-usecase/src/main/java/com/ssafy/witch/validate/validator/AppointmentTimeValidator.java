package com.ssafy.witch.validate.validator;

import com.ssafy.witch.validate.annotation.AppointmentTime;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class AppointmentTimeValidator implements
    ConstraintValidator<AppointmentTime, LocalDateTime> {

  @Override
  public boolean isValid(LocalDateTime time,
      ConstraintValidatorContext constraintValidatorContext) {

    if (time == null) {
      return true;
    }

    return time.getMinute() % 10 == 0;
  }
}
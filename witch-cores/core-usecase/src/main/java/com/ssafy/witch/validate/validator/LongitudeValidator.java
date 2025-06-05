package com.ssafy.witch.validate.validator;

import com.ssafy.witch.validate.annotation.Longitude;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LongitudeValidator implements ConstraintValidator<Longitude, Double> {

  @Override
  public boolean isValid(Double value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    return value >= -180.0 && value <= 180.0;
  }
}

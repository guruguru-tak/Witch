package com.ssafy.witch.validate.validator;

import com.ssafy.witch.validate.annotation.Latitude;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LatitudeValidator implements ConstraintValidator<Latitude, Double> {

  @Override
  public boolean isValid(Double value, ConstraintValidatorContext context) {
    if (value == null) {
      return true; // `@NotNull`과 함께 사용하도록 권장
    }
    return value >= -90.0 && value <= 90.0;
  }
}

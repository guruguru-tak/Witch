package com.ssafy.witch.validate.validator;

import com.ssafy.witch.validate.annotation.WitchEmail;

public class WitchEmailValidator extends RegexValidator<WitchEmail, String> {

  @Override
  protected String getRegex() {
    return ValidationRule.EMAIL.getRegex();
  }

  @Override
  protected String getErrorMessage() {
    return ValidationRule.EMAIL.getErrorMessage();
  }
}

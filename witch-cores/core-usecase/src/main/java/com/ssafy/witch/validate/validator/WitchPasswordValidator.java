package com.ssafy.witch.validate.validator;

import com.ssafy.witch.validate.annotation.WitchPassword;

public class WitchPasswordValidator extends RegexValidator<WitchPassword, String> {

  @Override
  protected String getRegex() {
    return ValidationRule.PASSWORD.getRegex();
  }

  @Override
  protected String getErrorMessage() {
    return ValidationRule.PASSWORD.getErrorMessage();
  }
}

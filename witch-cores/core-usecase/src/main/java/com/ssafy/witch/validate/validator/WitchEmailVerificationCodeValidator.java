package com.ssafy.witch.validate.validator;

import com.ssafy.witch.validate.annotation.WitchEmailVerificationCode;

public class WitchEmailVerificationCodeValidator extends
    RegexValidator<WitchEmailVerificationCode, String> {

  @Override
  protected String getRegex() {
    return ValidationRule.EMAIL_VERIFICATION_CODE.getRegex();
  }

  @Override
  protected String getErrorMessage() {
    return ValidationRule.EMAIL_VERIFICATION_CODE.getErrorMessage();
  }
}

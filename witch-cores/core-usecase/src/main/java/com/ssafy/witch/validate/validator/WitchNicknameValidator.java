package com.ssafy.witch.validate.validator;

import com.ssafy.witch.validate.annotation.WitchNickname;

public class WitchNicknameValidator extends RegexValidator<WitchNickname, String> {

  @Override
  protected String getRegex() {
    return ValidationRule.NICKNAME.getRegex();
  }

  @Override
  protected String getErrorMessage() {
    return ValidationRule.NICKNAME.getErrorMessage();
  }
}

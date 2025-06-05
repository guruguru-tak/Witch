package com.ssafy.witch.validate.validator;

import com.ssafy.witch.validate.annotation.GroupName;

public class GroupNameValidator extends RegexValidator<GroupName, String> {

  @Override
  protected String getRegex() {
    return ValidationRule.GROUP_NAME.getRegex();
  }

  @Override
  protected String getErrorMessage() {
    return ValidationRule.GROUP_NAME.getErrorMessage();
  }
}

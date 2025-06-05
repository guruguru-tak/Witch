package com.ssafy.witch.validate.validator;

import lombok.Getter;

@Getter
public enum ValidationRule {
  EMAIL("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", "올바른 형식의 이메일 주소여야 합니다"),
  EMAIL_VERIFICATION_CODE("^\\d{6}$", "이메일 인증 코드는 6자리 숫자여야 합니다"),
  NICKNAME("^[a-zA-Z0-9가-힣]{2,8}$", "닉네임은 영문 or 숫자 or 한글의 2-8자의 문자열이어야 합니다"),
  PASSWORD("^[a-zA-Z0-9!@#$%^&*()]{8,16}$", "패스워드는 영문 or 숫자 or 특수문자[!@#$%^&*]의 8-16자의 문자열이어야 합니다"),
  GROUP_NAME("^.{1,10}$", "모임 이름은 1글자 이상 10글자 이하여야 합니다"),
  APPOINTMENT_NAME("^.{1,10}$", "약속 이름은 1글자 이상 30글자 이하여야 합니다.");

  private final String regex;
  private final String errorMessage;

  ValidationRule(String regex, String errorMessage) {
    this.regex = regex;
    this.errorMessage = errorMessage;
  }
}

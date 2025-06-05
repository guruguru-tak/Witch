package com.ssafy.witch.user;

import lombok.Getter;

@Getter
public class EmailVerificationCodeGeneratedEvent {

  private String email;
  private EmailVerificationCode code;

  private EmailVerificationCodeGeneratedEvent(String email, EmailVerificationCode code) {
    this.email = email;
    this.code = code;
  }

  public static EmailVerificationCodeGeneratedEvent of(String email, EmailVerificationCode code) {
    return new EmailVerificationCodeGeneratedEvent(email, code);
  }
}

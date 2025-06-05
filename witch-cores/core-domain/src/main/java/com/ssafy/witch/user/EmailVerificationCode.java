package com.ssafy.witch.user;

import java.util.Objects;
import lombok.Getter;

@Getter
public class EmailVerificationCode {

  private final String code;

  private EmailVerificationCode(String code) {
    this.code = code;
  }

  public static EmailVerificationCode of(String code) {
    return new EmailVerificationCode(code);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EmailVerificationCode that = (EmailVerificationCode) o;
    return Objects.equals(code, that.code);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(code);
  }

}

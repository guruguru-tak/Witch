package com.ssafy.witch.user.command;

import com.ssafy.witch.validate.SelfValidating;
import com.ssafy.witch.validate.annotation.WitchEmail;
import com.ssafy.witch.validate.annotation.WitchEmailVerificationCode;
import com.ssafy.witch.validate.annotation.WitchNickname;
import com.ssafy.witch.validate.annotation.WitchPassword;
import lombok.Getter;

@Getter
public class SignupUserCommand extends SelfValidating<SignupUserCommand> {

  @WitchEmail
  private final String email;

  @WitchPassword
  private final String password;

  @WitchNickname
  private final String nickname;

  @WitchEmailVerificationCode
  private final String emailVerificationCode;

  public SignupUserCommand(String email, String password, String nickname,
      String emailVerificationCode) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.emailVerificationCode = emailVerificationCode;

    this.validateSelf();
  }
}

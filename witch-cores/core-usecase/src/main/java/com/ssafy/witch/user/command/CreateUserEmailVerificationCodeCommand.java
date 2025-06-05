package com.ssafy.witch.user.command;

import com.ssafy.witch.validate.SelfValidating;
import com.ssafy.witch.validate.annotation.WitchEmail;
import lombok.Getter;

@Getter
public class CreateUserEmailVerificationCodeCommand extends
    SelfValidating<CreateUserEmailVerificationCodeCommand> {

  @WitchEmail
  private final String email;

  public CreateUserEmailVerificationCodeCommand(String email) {
    this.email = email;
    this.validateSelf();
  }
}

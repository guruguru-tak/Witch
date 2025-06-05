package com.ssafy.witch.user.command;

import com.ssafy.witch.validate.SelfValidating;
import com.ssafy.witch.validate.annotation.WitchEmailVerificationCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class VerifyUserEmailVerificationCodeCommand extends
    SelfValidating<VerifyUserEmailVerificationCodeCommand> {

  @NotBlank
  @Email
  private String email;

  @WitchEmailVerificationCode
  private String emailVerificationCode;

  public VerifyUserEmailVerificationCodeCommand(String email, String emailVerificationCode) {
    this.email = email;
    this.emailVerificationCode = emailVerificationCode;

    this.validateSelf();
  }

}

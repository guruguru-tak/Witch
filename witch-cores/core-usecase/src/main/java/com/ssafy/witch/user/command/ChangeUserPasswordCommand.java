package com.ssafy.witch.user.command;

import com.ssafy.witch.validate.SelfValidating;
import com.ssafy.witch.validate.annotation.WitchPassword;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChangeUserPasswordCommand extends SelfValidating<ChangeUserPasswordCommand> {

  private final String userId;

  @NotNull
  private final String password;

  @WitchPassword
  private final String newPassword;

  public ChangeUserPasswordCommand(String userId, String password, String newPassword) {
    this.userId = userId;
    this.password = password;
    this.newPassword = newPassword;

    this.validateSelf();
  }

}

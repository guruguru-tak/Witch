package com.ssafy.witch.user.command;

import com.ssafy.witch.validate.SelfValidating;
import com.ssafy.witch.validate.annotation.WitchNickname;
import lombok.Getter;

@Getter
public class ChangeUserNicknameCommand extends SelfValidating<ChangeUserNicknameCommand> {

  private final String userId;

  @WitchNickname
  private final String nickname;

  public ChangeUserNicknameCommand(String userId, String nickname) {
    this.userId = userId;
    this.nickname = nickname;

    this.validateSelf();
  }
}

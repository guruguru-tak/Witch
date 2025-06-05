package com.ssafy.witch.user;

import com.ssafy.witch.user.command.ChangeUserNicknameCommand;
import com.ssafy.witch.user.command.ChangeUserPasswordCommand;

public interface ChangeUserInformationUseCase {

  void changeUserNickname(ChangeUserNicknameCommand command);

  void changePassword(ChangeUserPasswordCommand command);
}

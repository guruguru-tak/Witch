package com.ssafy.witch.user;

import com.ssafy.witch.user.command.SignupUserCommand;

public interface SignupUserUseCase {

  void signup(SignupUserCommand command);

}

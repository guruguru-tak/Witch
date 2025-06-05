package com.ssafy.witch.user;

import com.ssafy.witch.user.command.CreateUserEmailVerificationCodeCommand;
import com.ssafy.witch.user.command.VerifyUserEmailVerificationCodeCommand;

public interface VerifyUserEmailUseCase {

  void createUserEmailVerificationCode(CreateUserEmailVerificationCodeCommand command);

  void verifyEmailVerificationCode(VerifyUserEmailVerificationCodeCommand command);

}

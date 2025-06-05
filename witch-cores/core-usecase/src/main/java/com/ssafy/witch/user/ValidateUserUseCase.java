package com.ssafy.witch.user;

public interface ValidateUserUseCase {

  void checkUserEmailDuplication(final String email);

  void checkUserNicknameDuplication(final String nickname);
}

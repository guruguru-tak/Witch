package com.ssafy.witch.user;

import com.ssafy.witch.exception.user.UserEmailDuplicatedException;
import com.ssafy.witch.exception.user.UserNicknameDuplicatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserValidateService implements ValidateUserUseCase {

  private final ValidateUserPort validateUserPort;

  @Override
  public void checkUserEmailDuplication(final String email) {
    if (validateUserPort.isEmailDuplicated(email)) {
      throw new UserEmailDuplicatedException();
    }
  }

  @Override
  public void checkUserNicknameDuplication(String nickname) {
    if (validateUserPort.isNicknameDuplicated(nickname)) {
      throw new UserNicknameDuplicatedException();
    }
  }

}

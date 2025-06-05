package com.ssafy.witch.user;

import com.ssafy.witch.exception.user.IncorrectPasswordException;
import com.ssafy.witch.exception.user.UserNotFoundException;
import com.ssafy.witch.user.command.ChangeUserNicknameCommand;
import com.ssafy.witch.user.command.ChangeUserPasswordCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChangeUserInformationService implements ChangeUserInformationUseCase {

  private final ValidateUserUseCase validateUserUseCase;

  private final UserPort userPort;

  private final PasswordEncoder passwordEncoder;

  @Transactional
  @Override
  public void changeUserNickname(ChangeUserNicknameCommand command) {
    String userId = command.getUserId();
    String nickname = command.getNickname();

    validateUserUseCase.checkUserNicknameDuplication(nickname);

    User user = userPort.findById(userId).orElseThrow(UserNotFoundException::new);
    user.changeNickname(nickname);

    userPort.save(user);
  }

  @Transactional
  @Override
  public void changePassword(ChangeUserPasswordCommand command) {
    String userId = command.getUserId();
    String password = command.getPassword();
    String newPassword = passwordEncoder.encode(command.getNewPassword());

    User user = userPort.findById(userId).orElseThrow(UserNotFoundException::new);

    validatePassword(user, password);

    user.changePassword(newPassword);

    userPort.save(user);
  }

  private void validatePassword(User user, String password) {
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new IncorrectPasswordException();
    }
  }

}

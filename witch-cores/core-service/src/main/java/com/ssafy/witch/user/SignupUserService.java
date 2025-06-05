package com.ssafy.witch.user;

import com.ssafy.witch.exception.user.UserEmailVerificationCodeNotValidException;
import com.ssafy.witch.user.command.SignupUserCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignupUserService implements SignupUserUseCase {

  private final ValidateUserUseCase validateUserUseCase;

  private final VerifiedEmailCachePort verifiedEmailCachePort;

  private final PasswordEncoder passwordEncoder;
  private final UserPort userPort;

  @Override
  public void signup(SignupUserCommand command) {
    String email = command.getEmail();
    String nickname = command.getNickname();

    validateUserUseCase.checkUserEmailDuplication(email);
    validateUserUseCase.checkUserNicknameDuplication(nickname);

    checkEmailVerified(command);

    String encodedPassword = encodePassword(command);

    User newUser = User.createNewUser(
        command.getEmail(),
        encodedPassword,
        command.getNickname()
    );

    userPort.save(newUser);

  }

  private String encodePassword(SignupUserCommand command) {
    return passwordEncoder.encode(command.getPassword());
  }

  private void checkEmailVerified(SignupUserCommand command) {
    if (!isEmailVerified(command)) {
      throw new UserEmailVerificationCodeNotValidException();
    }
  }

  private boolean isEmailVerified(SignupUserCommand command) {
    String email = command.getEmail();
    EmailVerificationCode code =
        EmailVerificationCode.of(command.getEmailVerificationCode());

    return verifiedEmailCachePort.has(email) &&
        verifiedEmailCachePort.get(email).equals(code);
  }

}

package com.ssafy.witch.user;

import com.ssafy.witch.exception.user.UserEmailVerificationCodeNotValidException;
import com.ssafy.witch.user.command.CreateUserEmailVerificationCodeCommand;
import com.ssafy.witch.user.command.VerifyUserEmailVerificationCodeCommand;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class VerifyUserEmailService implements VerifyUserEmailUseCase {

  private final ValidateUserUseCase validateUserUseCase;

  private final EmailVerificationCodeGeneratorPort emailVerificationCodeGeneratorPort;

  private final EmailVerificationCodeCachePort emailVerificationCodeCachePort;
  private final VerifiedEmailCachePort verifiedEmailCachePort;

  private final EmailVerificationGeneratedEventPort emailVerificationGeneratedEventPort;

  @Value("${witch.email.verification-code.expire-minute}")
  private int emailVerificationCodeExpireMinute;

  @Value("${witch.email.verified.expire-minute}")
  private int verifiedEmailExpireMinute;


  @Transactional
  @Override
  public void createUserEmailVerificationCode(CreateUserEmailVerificationCodeCommand command) {

    String email = command.getEmail();
    validateUserUseCase.checkUserEmailDuplication(email);

    EmailVerificationCode code = emailVerificationCodeGeneratorPort.generate(email);

    emailVerificationGeneratedEventPort.publish(
        EmailVerificationCodeGeneratedEvent.of(email, code));

    emailVerificationCodeCachePort.upsert(email, code,
        Duration.ofMinutes(emailVerificationCodeExpireMinute));
  }

  @Transactional
  @Override
  public void verifyEmailVerificationCode(VerifyUserEmailVerificationCodeCommand command) {
    String email = command.getEmail();
    EmailVerificationCode code = EmailVerificationCode.of(command.getEmailVerificationCode());

    validateUserUseCase.checkUserEmailDuplication(email);

    if (!isEmailVerificationCodeValid(email, code)) {
      throw new UserEmailVerificationCodeNotValidException();
    }

    emailVerificationCodeCachePort.remove(email);
    verifiedEmailCachePort.upsert(email, code, Duration.ofMinutes(verifiedEmailExpireMinute));
  }

  private boolean isEmailVerificationCodeValid(String email, EmailVerificationCode code) {
    return emailVerificationCodeCachePort.has(email) &&
        emailVerificationCodeCachePort.get(email).equals(code);
  }
}

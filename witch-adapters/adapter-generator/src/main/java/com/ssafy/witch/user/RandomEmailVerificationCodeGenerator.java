package com.ssafy.witch.user;

import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class RandomEmailVerificationCodeGenerator implements EmailVerificationCodeGeneratorPort {

  private static final int MAX_VALUE = 999_999;

  @Override
  public EmailVerificationCode generate(String email) {
    int number = new Random().nextInt(MAX_VALUE + 1);
    String code = String.format("%06d", number);

    return EmailVerificationCode.of(code);
  }
}

package com.ssafy.witch.user;

public interface EmailVerificationCodeGeneratorPort {

  EmailVerificationCode generate(String email);

}

package com.ssafy.witch.user;

public interface EmailVerificationGeneratedEventPort {

  void publish(EmailVerificationCodeGeneratedEvent event);
}

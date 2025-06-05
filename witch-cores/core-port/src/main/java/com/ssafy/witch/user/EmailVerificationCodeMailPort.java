package com.ssafy.witch.user;

public interface EmailVerificationCodeMailPort {

  void send(String email, EmailVerificationCode code);

}

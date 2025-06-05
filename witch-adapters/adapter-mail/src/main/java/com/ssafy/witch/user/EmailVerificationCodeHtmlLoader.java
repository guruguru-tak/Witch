package com.ssafy.witch.user;

public interface EmailVerificationCodeHtmlLoader {

  String loadWith(EmailVerificationCode code);
}

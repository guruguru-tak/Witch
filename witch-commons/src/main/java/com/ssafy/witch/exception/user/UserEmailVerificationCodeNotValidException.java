package com.ssafy.witch.exception.user;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class UserEmailVerificationCodeNotValidException extends BusinessException {

  public UserEmailVerificationCodeNotValidException() {
    super(ErrorCode.EMAIL_VERIFICATION_CODE_MISMATCH);
  }
}

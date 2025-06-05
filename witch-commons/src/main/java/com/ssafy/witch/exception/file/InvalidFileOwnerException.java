package com.ssafy.witch.exception.file;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class InvalidFileOwnerException extends BusinessException {

  public InvalidFileOwnerException() {
    super(ErrorCode.INVALID_FILE_OWNER);
  }
}

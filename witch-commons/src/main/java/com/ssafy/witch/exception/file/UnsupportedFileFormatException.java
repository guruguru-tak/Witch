package com.ssafy.witch.exception.file;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;

public class UnsupportedFileFormatException extends BusinessException {

  public UnsupportedFileFormatException() {
    super(ErrorCode.UNSUPPORTED_FILE_FORMAT);
  }

}

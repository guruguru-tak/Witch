package com.ssafy.witch.advice;

import com.ssafy.witch.exception.BusinessException;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.response.WitchApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class BusinessExceptionAdvice {

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected WitchApiResponse<?> handleConstraintViolationException(ConstraintViolationException e) {
    log.error("error={}", e.getMessage(), e);
    return WitchApiResponse.failure(ErrorCode.INVALID_FORMAT, e.getLocalizedMessage());
  }

  @ExceptionHandler(BusinessException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected WitchApiResponse<?> handleUserException(BusinessException e) {
    log.error("error={}", e.getMessage(), e);
    return WitchApiResponse.failure(e.getErrorCode());
  }
}

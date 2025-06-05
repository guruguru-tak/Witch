package com.ssafy.witch.advice;

import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.response.WitchApiResponse;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

  // HTTP 요청 방식이 잘못된 경우 (Method Not Allowed)
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected WitchApiResponse<?> handleHttpRequestMethodNotSupportedException(Exception e) {
    log.error("error={}", e.getMessage(), e);
    // 예시: 클라이언트가 GET 요청을 보내야 하는 API에 POST 요청을 보내는 경우
    return WitchApiResponse.failure(ErrorCode.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(HandlerMethodValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected WitchApiResponse<?> handleHandlerMethodValidationException(
      HandlerMethodValidationException e) {
    log.error("error={}", e.getMessage(), e);

    String errorMessage = e.getAllValidationResults().stream()
        .flatMap(validationResult -> validationResult.getResolvableErrors().stream())
        .map(error -> {
          String[] codes = error.getCodes();
          String field = codes != null && codes.length > 0 ?
              extractFieldName(codes[0]) : "unknown";
          return field + ": " + error.getDefaultMessage();
        })
        .collect(Collectors.joining(", "));

    return WitchApiResponse.failure(ErrorCode.REQUIRED_FIELD_MISSING_OR_INVALID, errorMessage);
  }

  private String extractFieldName(String code) {
    if (code.contains(".")) {
      return code.substring(code.lastIndexOf(".") + 1);
    }
    return code;
  }

  // 요청 파라미터 검증 실패 (Validation Error)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected WitchApiResponse<?> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    log.error("error={}", e.getMessage(), e);

    BindingResult bindingResult = e.getBindingResult();

    String errorMessage = bindingResult.getFieldErrors().stream()
        .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
        .collect(Collectors.joining(", "));

    // 예시: @NotNull이 붙은 필드가 비어있는 경우 (예: 사용자 이메일 필드가 비어있음)
    return WitchApiResponse.failure(ErrorCode.REQUIRED_FIELD_MISSING_OR_INVALID, errorMessage);
  }

  // HTTP 바디 형식이 잘못된 경우 (Malformed Request Body)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected WitchApiResponse<?> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException e) {
    log.error("error={}", e.getMessage(), e);
    // 예시: JSON 형식이 잘못되어 파싱할 수 없는 경우 (예: 중괄호가 닫히지 않거나, 잘못된 구문)
    // 예시: int 타입에 string 타입이 들어왔을 경우
    return WitchApiResponse.failure(ErrorCode.INVALID_REQUEST_BODY);
  }

  // 요청 파라미터 타입이 일치하지 않는 경우 (path variable, query param)
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected WitchApiResponse<?> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException e) {
    log.error("error={}", e.getMessage(), e);
    String errorMessage = e.getName() + ": " + e.getValue();
    return WitchApiResponse.failure(ErrorCode.INVALID_PARAMETER, errorMessage);
  }

  // 필수 요청 파라미터가 누락된 경우 (Missing Required Parameter)
  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected WitchApiResponse<?> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException e) {
    log.error("error={}", e.getMessage(), e);
    // 예시: API 호출 시 필수 파라미터 `username`이 빠진 경우
    return WitchApiResponse.failure(ErrorCode.MISSING_REQUIRED_PARAMETER, e.getParameterName());
  }

  // 요청 URL에 대해 처리할 핸들러가 없을 경우
  @ExceptionHandler(NoResourceFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  protected WitchApiResponse<?> handleNoResourceFoundException(NoResourceFoundException e) {
    log.error("error={}", e.getMessage(), e);
    return WitchApiResponse.failure(ErrorCode.NOT_FOUND);
  }

  // 내부 서버 오류 (Internal Server Error)
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  protected WitchApiResponse<?> handleInternalServerError(Exception e) {
    log.error("error={}", e.getMessage(), e);
    // 예시: 예기치 않은 예외가 발생한 경우 (예: NullPointerException, 외부 서비스 호출 실패 등)
    return WitchApiResponse.failure(ErrorCode.INTERNAL_SERVER_ERROR);
  }
}

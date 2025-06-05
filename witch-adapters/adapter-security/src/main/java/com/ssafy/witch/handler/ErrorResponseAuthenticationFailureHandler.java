package com.ssafy.witch.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.response.WitchApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@RequiredArgsConstructor
public class ErrorResponseAuthenticationFailureHandler implements AuthenticationFailureHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException {
    ErrorCode errorCode = ErrorCode.INCORRECT_PASSWORD;
    sendResponse(response, errorCode);
  }

  private void sendResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
    String body = objectMapper.writeValueAsString(WitchApiResponse.failure(errorCode));

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.getWriter().write(body);
  }
}

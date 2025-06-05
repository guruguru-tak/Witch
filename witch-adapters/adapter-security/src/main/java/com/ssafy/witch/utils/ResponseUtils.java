package com.ssafy.witch.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.jwt.response.TokenResponse;
import com.ssafy.witch.response.WitchApiResponse;
import com.ssafy.witch.response.WitchError;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ResponseUtils {

  private ResponseUtils() {
  }

  public static void sendResponse(HttpServletResponse response, TokenResponse tokenResponse,
      ObjectMapper objectMapper) throws IOException {
    String body = objectMapper.writeValueAsString(WitchApiResponse.success(tokenResponse));

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.getWriter().write(body);
    response.setStatus(HttpStatus.OK.value());
  }

  public static void sendUnauthorizedResponse(HttpServletResponse response, ErrorCode errorCode,
      ObjectMapper objectMapper)
      throws IOException {
    WitchApiResponse<WitchError> errorResponse = WitchApiResponse.failure(errorCode);

    String body = objectMapper.writeValueAsString(errorResponse);
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.getWriter().write(body);
  }

}

package com.ssafy.witch.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.auth.JwtAuthException;
import com.ssafy.witch.utils.JwtConst;
import com.ssafy.witch.utils.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;


  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {

    JwtAuthException jwtAuthException =
        (JwtAuthException) request.getAttribute(JwtConst.JWT_AUTH_EXCEPTION_ATTRIBUTE_KEY);

    if (Objects.nonNull(jwtAuthException)) {
      ResponseUtils.sendUnauthorizedResponse(response, jwtAuthException.getErrorCode(),
          objectMapper);
      return;
    }

    ResponseUtils.sendUnauthorizedResponse(response, ErrorCode.UNAUTHORIZED, objectMapper);
  }


}

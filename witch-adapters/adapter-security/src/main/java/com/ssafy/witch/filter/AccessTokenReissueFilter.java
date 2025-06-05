package com.ssafy.witch.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.witch.exception.auth.JwtAuthException;
import com.ssafy.witch.jwt.JwtService;
import com.ssafy.witch.jwt.response.TokenResponse;
import com.ssafy.witch.utils.JwtConst;
import com.ssafy.witch.utils.ResponseUtils;
import com.ssafy.witch.utils.TokenExtractUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class AccessTokenReissueFilter extends OncePerRequestFilter {

  private static final AntPathRequestMatcher PATH_REQUEST_MATCHER
      = new AntPathRequestMatcher("/auth/token/reissue", "POST");

  private final JwtService jwtService;
  private final ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    if (!PATH_REQUEST_MATCHER.matches(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    String refreshToken =
        TokenExtractUtils.extractRefreshTokenFromBody(request, objectMapper);

    try {
      TokenResponse tokenResponse = jwtService.reissue(refreshToken);
      ResponseUtils.sendResponse(response, tokenResponse, objectMapper);
      return;
    } catch (JwtAuthException e) {
      request.setAttribute(JwtConst.JWT_AUTH_EXCEPTION_ATTRIBUTE_KEY, e);
    }

    filterChain.doFilter(request, response);
  }

}

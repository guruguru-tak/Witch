package com.ssafy.witch.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import org.springframework.util.StringUtils;

public class TokenExtractUtils {

  private static final String REFRESH_TOKEN_KEY = "refreshToken";
  private static final String ACCESS_TOKEN_HEADER_NAME = "Authorization";

  private TokenExtractUtils() {
  }

  public static String extractRefreshTokenFromBody(HttpServletRequest request,
      ObjectMapper objectMapper) throws IOException {
    Map<String, String> refreshTokenMap = objectMapper.readValue(request.getInputStream(),
        new TypeReference<>() {
        });

    String refreshTokenWithType = refreshTokenMap.get(REFRESH_TOKEN_KEY);

    if (!StringUtils.hasText(refreshTokenWithType) ||
        !refreshTokenWithType.startsWith(JwtConst.TOKEN_TYPE + " ")) {
      return null;
    }

    return refreshTokenWithType.substring(JwtConst.TOKEN_TYPE.length() + 1);
  }

  public static String extractAccessTokenFromHeader(HttpServletRequest request) {
    String accessTokenWithType = request.getHeader(ACCESS_TOKEN_HEADER_NAME);

    if (!StringUtils.hasText(accessTokenWithType) ||
        !accessTokenWithType.startsWith(JwtConst.TOKEN_TYPE + " ")) {
      return null;
    }
    return accessTokenWithType.substring(JwtConst.TOKEN_TYPE.length() + 1);
  }

}

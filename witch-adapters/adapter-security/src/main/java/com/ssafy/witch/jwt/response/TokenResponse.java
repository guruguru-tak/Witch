package com.ssafy.witch.jwt.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssafy.witch.utils.JwtConst;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TokenResponse {

  private final String tokenType = JwtConst.TOKEN_TYPE;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String accessToken;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long accessTokenExpiresIn;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String refreshToken;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long refreshTokenExpiresIn;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long refreshTokenRenewAvailableSeconds;

  public static TokenResponse reissue(String accessToken,
      Long accessTokenExpiresIn) {
    return new TokenResponse(accessToken, accessTokenExpiresIn, null, null, null);
  }

  public static TokenResponse create(String accessToken,
      Long accessTokenExpiresIn,
      String refreshToken, Long refreshTokenExpiresIn, Long refreshTokenRenewAvailableSeconds) {
    return new TokenResponse(accessToken, accessTokenExpiresIn, refreshToken,
        refreshTokenExpiresIn, refreshTokenRenewAvailableSeconds);
  }
}

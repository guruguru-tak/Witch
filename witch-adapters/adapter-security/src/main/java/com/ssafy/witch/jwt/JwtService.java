package com.ssafy.witch.jwt;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.ssafy.witch.exception.auth.InvalidAccessTokenException;
import com.ssafy.witch.exception.auth.InvalidRefreshTokenException;
import com.ssafy.witch.exception.auth.RefreshTokenNotRenewableException;
import com.ssafy.witch.jwt.response.TokenResponse;
import com.ssafy.witch.port.RefreshTokenCachePort;
import com.ssafy.witch.user.WitchUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
public class JwtService {

  private static final String ACCESS_TOKEN_SUBJECT = "access-token";
  private static final String REFRESH_TOKEN_SUBJECT = "refresh-token";

  private static final String USER_ID_CLAIM = "userId";
  private static final String EMAIL_CLAIM = "email";
  private static final String ROLES_CLAIM = "roles";

  private final RefreshTokenCachePort refreshTokenCachePort;
  private final JwtProperties jwtProperties;

  public WitchUserDetails resolveAccessToken(String accessToken) {
    Claims claims = validateAccessToken(accessToken);

    String userId = parseUserIdFrom(claims);
    String email = parseEmailFrom(claims);
    List<String> roles = parseRolesFrom(claims);

    return WitchUserDetails.builder()
        .userId(userId)
        .email(email)
        .roles(roles)
        .build();
  }

  private Claims validateAccessToken(String accessToken) {
    SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    if (!StringUtils.hasText(accessToken)) {
      throw new InvalidAccessTokenException();
    }

    try {
      return Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(accessToken)
          .getPayload();
    } catch (JwtException e) {
      throw new InvalidAccessTokenException();
    }
  }

  @Transactional
  public TokenResponse create(String userId, String email, List<String> roles) {
    String newAccessToken = createAccessToken(userId, email, roles);
    String newRefreshToken = createRefreshToken(userId, email, roles);

    refreshTokenCachePort.upsert(
        email,
        newRefreshToken,
        Duration.ofSeconds(jwtProperties.getRefreshTokenExpirationSeconds())
    );

    return TokenResponse.create(
        newAccessToken,
        jwtProperties.getAccessTokenExpirationSeconds(),
        newRefreshToken,
        jwtProperties.getRefreshTokenExpirationSeconds(),
        jwtProperties.getRefreshTokenRenewAvailableSeconds()
    );
  }

  @Transactional
  public TokenResponse reissue(String refreshToken) {
    Claims claims = validateRefreshToken(refreshToken);

    String userId = parseUserIdFrom(claims);
    String email = parseEmailFrom(claims);
    List<String> roles = parseRolesFrom(claims);

    String newAccessToken = createAccessToken(userId, email, roles);

    return TokenResponse.reissue(
        newAccessToken,
        jwtProperties.getAccessTokenExpirationSeconds());
  }

  private List<String> parseRolesFrom(Claims claims) {
    return ((List<?>) claims.get(ROLES_CLAIM))
        .stream()
        .map(Object::toString)
        .toList();
  }

  private String parseEmailFrom(Claims claims) {
    return claims.get(EMAIL_CLAIM, String.class);
  }

  private String parseUserIdFrom(Claims claims) {
    return claims.get(USER_ID_CLAIM, String.class);
  }

  @Transactional
  public TokenResponse renew(String refreshToken) {
    Claims claims = validateRefreshToken(refreshToken);

    Date expiration = claims.getExpiration();
    long remainingMillis = expiration.getTime() - System.currentTimeMillis();
    if (remainingMillis > TimeUnit.SECONDS.toMillis(
        jwtProperties.getRefreshTokenRenewAvailableSeconds())) {
      throw new RefreshTokenNotRenewableException();
    }

    String userId = parseUserIdFrom(claims);
    String email = parseEmailFrom(claims);
    List<String> roles = parseRolesFrom(claims);

    String newAccessToken = createAccessToken(userId, email, roles);
    String newRefreshToken = createRefreshToken(userId, email, roles);

    refreshTokenCachePort.upsert(
        email,
        newRefreshToken,
        Duration.ofSeconds(jwtProperties.getRefreshTokenExpirationSeconds())
    );

    return TokenResponse.create(
        newAccessToken,
        jwtProperties.getAccessTokenExpirationSeconds(),
        newRefreshToken,
        jwtProperties.getRefreshTokenExpirationSeconds(),
        jwtProperties.getRefreshTokenRenewAvailableSeconds()
    );
  }

  // 공통 리프레시 토큰 검증 로직
  private Claims validateRefreshToken(String refreshToken) {
    if (!StringUtils.hasText(refreshToken)) {
      throw new InvalidRefreshTokenException();
    }

    SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));

    try {
      Claims claims = Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(refreshToken)
          .getPayload();

      String email = parseEmailFrom(claims);
      String cachedToken = refreshTokenCachePort.get(email)
          .orElseThrow(InvalidRefreshTokenException::new);

      if (!refreshToken.equals(cachedToken)) {
        throw new InvalidRefreshTokenException();
      }

      return claims;

    } catch (JwtException e) {
      throw new InvalidRefreshTokenException();
    }

  }

  private String createAccessToken(String userId, String email, List<String> roles) {
    return buildToken(
        userId,
        email,
        roles,
        ACCESS_TOKEN_SUBJECT,
        jwtProperties.getAccessTokenExpirationSeconds()
    );
  }

  private String createRefreshToken(String userId, String email, List<String> roles) {
    return buildToken(
        userId,
        email,
        roles,
        REFRESH_TOKEN_SUBJECT,
        jwtProperties.getRefreshTokenExpirationSeconds()
    );
  }

  private String buildToken(String userId, String email, List<String> roles, String subject,
      Long expirationSeconds) {
    SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));

    return Jwts.builder()
        .subject(subject)
        .claim(USER_ID_CLAIM, userId)
        .claim(EMAIL_CLAIM, email)
        .claim(ROLES_CLAIM, roles)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(
            new Date(System.currentTimeMillis() + SECONDS.toMillis(expirationSeconds)))
        .signWith(key)
        .compact();
  }
}

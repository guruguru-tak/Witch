package com.ssafy.witch.jwt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class JwtProperties {

  @Value("${witch.jwt.properties.secret}")
  private String secret;

  @Value("${witch.jwt.properties.access-token.expiration-seconds}")
  private Long accessTokenExpirationSeconds;

  @Value("${witch.jwt.properties.refresh-token.expiration-seconds}")
  private Long refreshTokenExpirationSeconds;

  @Value("${witch.jwt.properties.refresh-token.renew-available-seconds}")
  private Long refreshTokenRenewAvailableSeconds;

}

package com.ssafy.witch.port;

import java.time.Duration;
import java.util.Optional;

public interface RefreshTokenCachePort {

  void upsert(String email, String refreshToken, Duration expireDuration);

  Optional<String> get(String email);

  void delete(String email);
}

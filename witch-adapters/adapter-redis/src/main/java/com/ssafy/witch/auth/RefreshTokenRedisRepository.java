package com.ssafy.witch.auth;

import com.ssafy.witch.port.RefreshTokenCachePort;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RefreshTokenRedisRepository implements RefreshTokenCachePort {

  private static final String KEY_PREFIX = "refreshToken:";

  private final StringRedisTemplate redisTemplate;

  @Override
  public void upsert(String email, String refreshToken, Duration expireDuration) {
    ValueOperations<String, String> ops = redisTemplate.opsForValue();
    ops.set(KEY_PREFIX + email, refreshToken, expireDuration);
  }


  @Override
  public Optional<String> get(String email) {
    ValueOperations<String, String> ops = redisTemplate.opsForValue();
    return Optional.ofNullable(ops.get(KEY_PREFIX + email));
  }

  @Override
  public void delete(String email) {
    redisTemplate.delete(KEY_PREFIX + email);
  }
}

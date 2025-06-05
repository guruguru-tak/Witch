package com.ssafy.witch.user;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class EmailVerificationCodeRedisRepository implements EmailVerificationCodeCachePort {

  private static final String KEY_PREFIX = "emailVerificationCode:";

  private final StringRedisTemplate redisTemplate;

  @Override
  public void upsert(String email, EmailVerificationCode code, Duration expireDuration) {
    ValueOperations<String, String> ops = redisTemplate.opsForValue();
    ops.set(KEY_PREFIX + email, code.getCode(), expireDuration);
  }

  @Override
  public EmailVerificationCode get(String email) {
    ValueOperations<String, String> ops = redisTemplate.opsForValue();
    String code = ops.get(KEY_PREFIX + email);
    return EmailVerificationCode.of(code);
  }

  @Override
  public void remove(String email) {
    redisTemplate.delete(KEY_PREFIX + email);
  }

  @Override
  public boolean has(String email) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(KEY_PREFIX + email));
  }
}

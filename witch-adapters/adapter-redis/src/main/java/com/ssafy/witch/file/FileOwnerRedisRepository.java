package com.ssafy.witch.file;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FileOwnerRedisRepository implements FileOwnerCachePort {

  private static final String KEY_PREFIX = "fileOwner:";
  private final StringRedisTemplate redisTemplate;

  @Override
  public void save(String objectKey, String ownerId, Duration ttl) {
    String key = KEY_PREFIX + objectKey;
    redisTemplate.opsForValue().set(key, ownerId, ttl);
  }

  @Override
  public String getOwnerId(String objectKey) {
    String key = KEY_PREFIX + objectKey;
    return redisTemplate.opsForValue().get(key);
  }

  @Override
  public void delete(String objectKey) {
    redisTemplate.delete(KEY_PREFIX + objectKey);
  }
}

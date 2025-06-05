package com.ssafy.witch.appointment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.witch.apoointment.AppointmentMemberPositionCachePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AppointmentMemberPositionRedisRepository implements
    AppointmentMemberPositionCachePort {

  private static final String KEY_PREFIX = "appointmentMemberPosition:";

  private final ObjectMapper objectMapper;
  private final StringRedisTemplate redisTemplate;

  @Override
  public void upsert(String userId, Position position) {
    ValueOperations<String, String> ops = redisTemplate.opsForValue();
    try {
      ops.set(KEY_PREFIX + userId, objectMapper.writeValueAsString(position));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage(), e);
    }
  }

  @Override
  public Position get(String appointmentId) {
    ValueOperations<String, String> ops = redisTemplate.opsForValue();
    try {
      String value = ops.get(KEY_PREFIX + appointmentId);
      if (value == null) {
        return new Position(null, null);
      }
      return objectMapper.readValue(value, Position.class);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }
}

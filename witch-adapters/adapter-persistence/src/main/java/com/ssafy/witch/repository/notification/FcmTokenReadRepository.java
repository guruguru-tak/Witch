package com.ssafy.witch.repository.notification;

import com.ssafy.witch.entity.notification.FcmTokenEntity;
import com.ssafy.witch.notification.FcmTokenWritePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FcmTokenReadRepository implements FcmTokenWritePort {

  private final FcmTokenJpaRepository fcmTokenJpaRepository;

  @Override
  public void deleteFcmToken(String fcmToken) {
    fcmTokenJpaRepository.deleteByFcmToken(fcmToken);
  }


  @Override
  public void saveFcmToken(String userid, String fcmToken) {
    fcmTokenJpaRepository.save(new FcmTokenEntity(null, userid, fcmToken));
  }

  @Override
  public void deleteByUserId(String userId) {
    fcmTokenJpaRepository.deleteByUserId(userId);
  }
}

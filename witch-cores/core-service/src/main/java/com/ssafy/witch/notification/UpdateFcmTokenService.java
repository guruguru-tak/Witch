package com.ssafy.witch.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UpdateFcmTokenService implements UpdateFcmTokenUseCase {

  private final FcmTokenWritePort fcmTokenWritePort;

  @Transactional
  @Override
  public void update(String userId, String fcmToken) {
    fcmTokenWritePort.deleteFcmToken(fcmToken);
    fcmTokenWritePort.deleteByUserId(userId);
    fcmTokenWritePort.saveFcmToken(userId, fcmToken);
  }
}

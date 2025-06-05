package com.ssafy.witch.notification;

public interface UpdateFcmTokenUseCase {

  void update(String userId, String fcmToken);
}

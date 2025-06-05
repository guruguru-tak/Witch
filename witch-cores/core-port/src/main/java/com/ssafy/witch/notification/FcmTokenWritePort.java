package com.ssafy.witch.notification;

public interface FcmTokenWritePort {

  void deleteFcmToken(String fcmToken);

  void saveFcmToken(String userid, String fcmToken);

  void deleteByUserId(String userId);
}

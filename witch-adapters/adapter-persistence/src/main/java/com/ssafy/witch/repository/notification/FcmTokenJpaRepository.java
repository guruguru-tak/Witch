package com.ssafy.witch.repository.notification;

import com.ssafy.witch.entity.notification.FcmTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmTokenJpaRepository extends JpaRepository<FcmTokenEntity, Long> {

  void deleteByFcmToken(String fcmToken);

  void deleteByUserId(String userId);
}

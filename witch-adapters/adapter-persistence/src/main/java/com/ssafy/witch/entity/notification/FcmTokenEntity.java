package com.ssafy.witch.entity.notification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "fcm_token")
@Entity
public class FcmTokenEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  private Long fcmTokenId;

  @JoinColumn(nullable = false, unique = true, updatable = false)
  private String userId;

  @Column(nullable = false, unique = true, updatable = false)
  private String fcmToken;

  public FcmTokenEntity(Long fcmTokenId, String userId, String fcmToken) {
    this.fcmTokenId = fcmTokenId;
    this.userId = userId;
    this.fcmToken = fcmToken;
  }
}

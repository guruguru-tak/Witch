package com.ssafy.witch.snack;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Snack {

  private String snackId;
  private String appointmentId;
  private String userId;
  private Double longitude;
  private Double latitude;
  private String snackImageUrl;
  private String snackSoundUrl;
  private LocalDateTime createdAt;

  public Snack(String snackId, String appointmentId, String userId, Double longitude,
      Double latitude,
      String snackImageUrl, String snackSoundUrl, LocalDateTime createdAt) {
    this.snackId = snackId;
    this.appointmentId = appointmentId;
    this.userId = userId;
    this.longitude = longitude;
    this.latitude = latitude;
    this.snackImageUrl = snackImageUrl;
    this.snackSoundUrl = snackSoundUrl;
    this.createdAt = createdAt;
  }

  public static Snack createNewSnack(String appointmentId, String userId, Double longitude,
      Double latitude, String snackImageUrl, String snackSoundUrl) {
    return new Snack(
        UUID.randomUUID().toString(),
        appointmentId,
        userId,
        longitude,
        latitude,
        snackImageUrl,
        snackSoundUrl,
        null
    );
  }
}

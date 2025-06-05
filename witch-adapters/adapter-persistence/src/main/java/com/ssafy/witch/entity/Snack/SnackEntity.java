package com.ssafy.witch.entity.Snack;

import com.ssafy.witch.entity.audit.MutableBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "snack")
@Entity
public class SnackEntity extends MutableBaseEntity {

  @Id
  @Column(unique = true, nullable = false)
  private String snackId;

  @JoinColumn(nullable = false)
  private String appointmentId;

  @JoinColumn(nullable = false)
  private String userId;

  @Column(nullable = false)
  private Double longitude;

  @Column(nullable = false)
  private Double latitude;

  private String snackImageUrl;
  private String snackSoundUrl;

  public SnackEntity(String snackId, String appointmentId, String userId, Double longitude,
      Double latitude, String snackImageUrl, String snackSoundUrl) {
    this.snackId = snackId;
    this.appointmentId = appointmentId;
    this.userId = userId;
    this.longitude = longitude;
    this.latitude = latitude;
    this.snackImageUrl = snackImageUrl;
    this.snackSoundUrl = snackSoundUrl;
  }
}

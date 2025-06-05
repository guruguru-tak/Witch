package com.ssafy.witch.entity.appointment;

import com.ssafy.witch.entity.audit.MutableBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "appointment")
@Entity
public class AppointmentEntity extends MutableBaseEntity {

  @Id
  @Column(unique = true, nullable = false)
  private String appointmentId;

  @Column(nullable = false)
  private String groupId;

  @Column(nullable = false)
  private String name;

  private String summary;

  @Column(nullable = false)
  private LocalDateTime appointmentTime;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private AppointmentEntityStatus status;

  @Column(nullable = false)
  private Double longitude;

  @Column(nullable = false)
  private Double latitude;

  private String address;

  public AppointmentEntity(String appointmentId, String groupId, String name, String summary,
      LocalDateTime appointmentTime, AppointmentEntityStatus status, Double longitude,
      Double latitude, String address) {
    this.appointmentId = appointmentId;
    this.groupId = groupId;
    this.name = name;
    this.summary = summary;
    this.appointmentTime = appointmentTime;
    this.status = status;
    this.longitude = longitude;
    this.latitude = latitude;
    this.address = address;
  }
}

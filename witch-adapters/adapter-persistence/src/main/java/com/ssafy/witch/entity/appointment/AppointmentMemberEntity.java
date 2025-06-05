package com.ssafy.witch.entity.appointment;

import com.ssafy.witch.entity.audit.MutableBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "appointment_member")
@Entity
public class AppointmentMemberEntity extends MutableBaseEntity {

  @Id
  @Column(unique = true, nullable = false)
  private String appointmentMemberId;

  @JoinColumn(nullable = false)
  private String userId;

  @JoinColumn(nullable = false)
  private String appointmentId;

  @Column(nullable = false)
  private boolean isLeader;

  private Double finalLatitude;
  private Double finalLongitude;

  public AppointmentMemberEntity(String appointmentMemberId, String userId, String appointmentId,
      boolean isLeader, Double finalLatitude, Double finalLongitude) {
    this.appointmentMemberId = appointmentMemberId;
    this.userId = userId;
    this.appointmentId = appointmentId;
    this.isLeader = isLeader;
    this.finalLatitude = finalLatitude;
    this.finalLongitude = finalLongitude;
  }
}

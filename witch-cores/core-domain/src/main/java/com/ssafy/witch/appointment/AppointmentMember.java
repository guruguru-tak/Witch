package com.ssafy.witch.appointment;

import java.util.UUID;
import lombok.Getter;

@Getter
public class AppointmentMember {


  private String appointmentMemberId;
  private String userId;
  private String appointmentId;
  private boolean isLeader;
  private Double finalLatitude;
  private Double finalLongitude;

  public AppointmentMember(String appointmentMemberId, String userId, String appointmentId,
      boolean isLeader, Double finalLatitude, Double finalLongitude) {
    this.appointmentMemberId = appointmentMemberId;
    this.userId = userId;
    this.appointmentId = appointmentId;
    this.isLeader = isLeader;
    this.finalLatitude = finalLatitude;
    this.finalLongitude = finalLongitude;
  }

  public static AppointmentMember createNewLeader(String userId, String appointmentId) {
    return new AppointmentMember(
        UUID.randomUUID().toString(),
        userId,
        appointmentId,
        true,
        null,
        null
    );
  }

  public static AppointmentMember createNewMember(String userId, String appointmentId) {
    return new AppointmentMember(
        UUID.randomUUID().toString(),
        userId,
        appointmentId,
        false,
        null,
        null
    );
  }
}

package com.ssafy.witch.apoointment.model;

import com.ssafy.witch.appointment.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class AppointmentDetailProjection {

  private String appointmentId;
  private String name;
  private AppointmentStatus appointmentStatus;
  private String summary;
  private LocalDateTime appointmentTime;
  private String address;
  private Double latitude;
  private Double longitude;

  private List<AppointmentMemberProjection> members;

  public void setMemberList(List<AppointmentMemberProjection> members) {
    this.members = members;
  }
}

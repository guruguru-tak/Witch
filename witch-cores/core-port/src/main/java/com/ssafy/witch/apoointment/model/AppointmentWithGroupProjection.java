package com.ssafy.witch.apoointment.model;

import com.ssafy.witch.appointment.AppointmentStatus;
import com.ssafy.witch.group.model.GroupProjection;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointmentWithGroupProjection {

  private final String appointmentId;
  private final String name;
  private final LocalDateTime appointmentTime;
  private final AppointmentStatus status;
  private final GroupProjection group;
}

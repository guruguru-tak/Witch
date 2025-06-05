package com.ssafy.witch.apoointment.model;

import com.ssafy.witch.appointment.AppointmentStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointmentProjection {

  private final String appointmentId;
  private final String name;
  private final LocalDateTime appointmentTime;
  private final AppointmentStatus status;
  private final boolean isMyAppointment;

}

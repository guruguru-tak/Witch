package com.ssafy.witch.appointment.output;

import com.ssafy.witch.appointment.AppointmentStatus;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AppointmentOutput {

  private final String appointmentId;
  private final String name;
  private final LocalDateTime appointmentTime;
  private final AppointmentStatus status;
  private final boolean isMyAppointment;

  public AppointmentOutput(String appointmentId, String name, LocalDateTime appointmentTime,
      AppointmentStatus status, boolean isMyAppointment) {
    this.appointmentId = appointmentId;
    this.name = name;
    this.appointmentTime = appointmentTime;
    this.status = status;
    this.isMyAppointment = isMyAppointment;
  }
}

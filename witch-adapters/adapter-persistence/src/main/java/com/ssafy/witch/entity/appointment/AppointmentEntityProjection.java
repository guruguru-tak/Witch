package com.ssafy.witch.entity.appointment;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointmentEntityProjection {

  private String appointmentId;
  private String name;
  private LocalDateTime appointmentTime;
  private AppointmentEntityStatus status;
  private Boolean isMyAppointment;
}

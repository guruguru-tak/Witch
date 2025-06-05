package com.ssafy.witch.controller.appointment.response;

import com.ssafy.witch.appointment.AppointmentStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppointmentResponse {

  private String appointmentId;
  private String name;
  private LocalDateTime appointmentTime;
  private AppointmentStatus status;
  private boolean isMyAppointment;

}

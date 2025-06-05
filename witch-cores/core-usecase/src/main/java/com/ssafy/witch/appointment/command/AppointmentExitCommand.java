package com.ssafy.witch.appointment.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointmentExitCommand {

  private String userId;
  private String appointmentId;

}

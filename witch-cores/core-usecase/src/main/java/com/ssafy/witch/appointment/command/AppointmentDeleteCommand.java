package com.ssafy.witch.appointment.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointmentDeleteCommand {

  private final String userId;
  private final String appointmentId;

}

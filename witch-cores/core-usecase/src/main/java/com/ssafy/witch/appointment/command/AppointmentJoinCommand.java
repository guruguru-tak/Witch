package com.ssafy.witch.appointment.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointmentJoinCommand {

  private String userId;
  private String appointmentId;

}

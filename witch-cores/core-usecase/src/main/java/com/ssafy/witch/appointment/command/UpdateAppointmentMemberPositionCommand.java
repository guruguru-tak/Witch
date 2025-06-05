package com.ssafy.witch.appointment.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateAppointmentMemberPositionCommand {

  private String userId;
  private String appointmentId;
  private double latitude;
  private double longitude;

}

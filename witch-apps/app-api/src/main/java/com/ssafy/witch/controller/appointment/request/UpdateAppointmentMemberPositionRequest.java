package com.ssafy.witch.controller.appointment.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UpdateAppointmentMemberPositionRequest {

  private double latitude;
  private double longitude;

}

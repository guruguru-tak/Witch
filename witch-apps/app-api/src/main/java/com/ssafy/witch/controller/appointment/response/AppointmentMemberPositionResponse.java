package com.ssafy.witch.controller.appointment.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointmentMemberPositionResponse {

  private String userId;
  private String nickname;
  private String profileImageUrl;
  private Double latitude;
  private Double longitude;

}

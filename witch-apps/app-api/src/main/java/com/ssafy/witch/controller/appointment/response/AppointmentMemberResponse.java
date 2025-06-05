package com.ssafy.witch.controller.appointment.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointmentMemberResponse {

  private String userId;
  private String nickname;
  private String profileImageUrl;
  private boolean isLeader;

}

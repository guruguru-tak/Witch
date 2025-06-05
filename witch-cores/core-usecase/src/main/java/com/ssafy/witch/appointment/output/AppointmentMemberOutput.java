package com.ssafy.witch.appointment.output;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointmentMemberOutput {

  private String userId;
  private String nickname;
  private String profileImageUrl;
  private boolean isLeader;
  private String fcmToken;

}

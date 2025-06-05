package com.ssafy.witch.apoointment.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class AppointmentMemberProjection {

  private String userId;
  private String nickname;
  private String profileImageUrl;
  private boolean isLeader;
  private String fcmToken;
}

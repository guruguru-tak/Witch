package com.ssafy.witch.user;

import com.ssafy.witch.apoointment.model.AppointmentMemberProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserNotification {

  private String userId;
  private String nickname;
  private String fcmToken;

  public static UserNotification of(AppointmentMemberProjection appointmentMemberProjection) {
    return new UserNotification(
        appointmentMemberProjection.getUserId(),
        appointmentMemberProjection.getNickname(),
        appointmentMemberProjection.getFcmToken());
  }
}

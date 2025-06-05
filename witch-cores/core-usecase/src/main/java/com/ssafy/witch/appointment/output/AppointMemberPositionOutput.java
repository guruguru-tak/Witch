package com.ssafy.witch.appointment.output;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointMemberPositionOutput {

  private String userId;
  private String nickname;
  private String profileImageUrl;
  private Double latitude;
  private Double longitude;

}

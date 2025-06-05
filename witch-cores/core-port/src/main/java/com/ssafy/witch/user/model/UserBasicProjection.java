package com.ssafy.witch.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserBasicProjection {

  private String userId;
  private String nickname;
  private String profileImageUrl;

}

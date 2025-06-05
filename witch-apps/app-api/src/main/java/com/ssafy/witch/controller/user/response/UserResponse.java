package com.ssafy.witch.controller.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserResponse {

  private String userId;
  private String nickname;
  private String profileImageUrl;

}

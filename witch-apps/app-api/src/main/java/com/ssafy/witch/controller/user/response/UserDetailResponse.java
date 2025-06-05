package com.ssafy.witch.controller.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDetailResponse {

  private String userId;
  private String email;
  private String nickname;
  private String profileImageUrl;

}

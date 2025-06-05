package com.ssafy.witch.user.output;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserBasicOutput {

  private String userId;
  private String nickname;
  private String profileImageUrl;

}

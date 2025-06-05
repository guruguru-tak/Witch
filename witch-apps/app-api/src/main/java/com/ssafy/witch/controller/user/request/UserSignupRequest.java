package com.ssafy.witch.controller.user.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class UserSignupRequest {

  private String email;

  private String password;

  private String nickname;

  private String emailVerificationCode;

}

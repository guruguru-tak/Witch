package com.ssafy.witch.user.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateProfileImageCommand {

  private String userId;
  private String objectKey;
}

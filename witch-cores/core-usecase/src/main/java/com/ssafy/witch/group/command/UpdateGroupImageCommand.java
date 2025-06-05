package com.ssafy.witch.group.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateGroupImageCommand {

  private String userId;
  private String groupId;
  private String objectKey;
}

package com.ssafy.witch.group.command;

import lombok.Getter;

@Getter
public class GroupJoinRequestCreateCommand {

  private final String userId;
  private final String groupId;

  public GroupJoinRequestCreateCommand(String userId, String groupId) {
    this.userId = userId;
    this.groupId = groupId;
  }
}

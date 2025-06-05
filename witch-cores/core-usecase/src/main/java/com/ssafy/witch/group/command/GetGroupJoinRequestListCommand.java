package com.ssafy.witch.group.command;

import lombok.Getter;

@Getter
public class GetGroupJoinRequestListCommand {

  private final String userId;
  private final String groupId;

  public GetGroupJoinRequestListCommand(String userId, String groupId) {
    this.userId = userId;
    this.groupId = groupId;
  }
}

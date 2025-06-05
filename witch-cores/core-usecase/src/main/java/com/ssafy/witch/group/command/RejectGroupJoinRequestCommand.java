package com.ssafy.witch.group.command;

import lombok.Getter;

@Getter
public class RejectGroupJoinRequestCommand {

  private final String userId;
  private final String joinRequestId;

  public RejectGroupJoinRequestCommand(String userId, String joinRequestId) {
    this.userId = userId;
    this.joinRequestId = joinRequestId;
  }
}

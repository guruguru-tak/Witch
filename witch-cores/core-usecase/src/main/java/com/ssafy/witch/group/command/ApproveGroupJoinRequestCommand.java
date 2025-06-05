package com.ssafy.witch.group.command;

import lombok.Getter;

@Getter
public class ApproveGroupJoinRequestCommand {

  private final String userId;
  private final String joinRequestId;

  public ApproveGroupJoinRequestCommand(String userId, String joinRequestId) {
    this.userId = userId;
    this.joinRequestId = joinRequestId;
  }
}

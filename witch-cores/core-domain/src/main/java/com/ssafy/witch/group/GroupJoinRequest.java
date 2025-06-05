package com.ssafy.witch.group;

import java.util.UUID;
import lombok.Getter;

@Getter
public class GroupJoinRequest {

  private String groupJoinRequestId;
  private String userId;
  private String groupId;

  public GroupJoinRequest(String groupJoinRequestId, String userId, String groupId) {
    this.groupJoinRequestId = groupJoinRequestId;
    this.userId = userId;
    this.groupId = groupId;
  }

  public static GroupJoinRequest of(String userId, String groupId) {
    return new GroupJoinRequest(UUID.randomUUID().toString(), userId, groupId);
  }
}

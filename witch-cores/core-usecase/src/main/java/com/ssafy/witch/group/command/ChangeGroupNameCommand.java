package com.ssafy.witch.group.command;

import com.ssafy.witch.validate.SelfValidating;
import com.ssafy.witch.validate.annotation.GroupName;
import lombok.Getter;

@Getter
public class ChangeGroupNameCommand extends SelfValidating<ChangeGroupNameCommand> {

  private final String userId;

  private final String groupId;

  @GroupName
  private final String name;

  public ChangeGroupNameCommand(String userId, String groupId, String name) {
    this.userId = userId;
    this.groupId = groupId;
    this.name = name;

    this.validateSelf();
  }
}

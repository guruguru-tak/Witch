package com.ssafy.witch.group.command;

import com.ssafy.witch.validate.SelfValidating;
import com.ssafy.witch.validate.annotation.GroupName;
import lombok.Getter;

@Getter
public class GroupCreateCommand extends SelfValidating<GroupCreateCommand> {

  private final String userId;

  @GroupName
  private final String name;

  private final String groupImageObjectKey;

  public GroupCreateCommand(String userId, String name, String groupImageObjectKey) {
    this.userId = userId;
    this.name = name;
    this.groupImageObjectKey = groupImageObjectKey;

    this.validateSelf();
  }
}

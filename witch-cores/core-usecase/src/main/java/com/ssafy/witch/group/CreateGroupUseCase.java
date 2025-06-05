package com.ssafy.witch.group;

import com.ssafy.witch.group.command.GroupCreateCommand;

public interface CreateGroupUseCase {

  void createGroup(GroupCreateCommand command);

}

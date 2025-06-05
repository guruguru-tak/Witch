package com.ssafy.witch.group;

import com.ssafy.witch.group.command.GroupJoinRequestCreateCommand;

public interface CreateGroupJoinRequestUseCase {

  void creatGroupJoinRequest(GroupJoinRequestCreateCommand command);

}

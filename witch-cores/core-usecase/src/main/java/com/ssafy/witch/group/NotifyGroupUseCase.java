package com.ssafy.witch.group;

import com.ssafy.witch.group.command.NotifyGroupJoinRequestApproveCommand;
import com.ssafy.witch.group.command.NotifyGroupJoinRequestCommand;
import com.ssafy.witch.group.command.NotifyGroupJoinRequestRejectCommand;

public interface NotifyGroupUseCase {

  void notifyJoinRequest(NotifyGroupJoinRequestCommand command);

  void notifyJoinRequestApproved(NotifyGroupJoinRequestApproveCommand command);

  void notifyJoinRequestReject(NotifyGroupJoinRequestRejectCommand command);
}

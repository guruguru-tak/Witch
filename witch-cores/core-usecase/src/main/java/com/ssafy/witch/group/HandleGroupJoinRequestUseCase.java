package com.ssafy.witch.group;

import com.ssafy.witch.group.command.ApproveGroupJoinRequestCommand;
import com.ssafy.witch.group.command.GetGroupJoinRequestListCommand;
import com.ssafy.witch.group.command.RejectGroupJoinRequestCommand;
import com.ssafy.witch.group.output.GroupJoinRequestListOutput;

public interface HandleGroupJoinRequestUseCase {

  void approveGroupJoinRequest(ApproveGroupJoinRequestCommand command);

  void rejectGroupJoinRequest(RejectGroupJoinRequestCommand command);

  GroupJoinRequestListOutput getGroupJoinRequestList(GetGroupJoinRequestListCommand command);
}

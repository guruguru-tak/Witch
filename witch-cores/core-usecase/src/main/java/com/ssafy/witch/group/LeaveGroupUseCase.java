package com.ssafy.witch.group;


import com.ssafy.witch.group.command.LeaveGroupCommand;

public interface LeaveGroupUseCase {

    void leaveGroup(LeaveGroupCommand command);
}

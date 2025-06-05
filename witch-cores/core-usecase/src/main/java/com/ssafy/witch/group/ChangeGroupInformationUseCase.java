package com.ssafy.witch.group;

import com.ssafy.witch.group.command.ChangeGroupNameCommand;

public interface ChangeGroupInformationUseCase {

  void changeGroupName(ChangeGroupNameCommand command);

}

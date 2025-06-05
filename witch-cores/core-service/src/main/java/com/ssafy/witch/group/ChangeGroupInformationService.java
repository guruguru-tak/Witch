package com.ssafy.witch.group;

import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.group.command.ChangeGroupNameCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChangeGroupInformationService implements ChangeGroupInformationUseCase {

  private final GroupValidateUseCase groupValidateUseCase;
  private final GroupPort groupPort;
  private final GroupMemberPort groupMemberPort;

  @Transactional
  @Override
  public void changeGroupName(ChangeGroupNameCommand command) {

    String userId = command.getUserId();
    String groupId = command.getGroupId();
    String name = command.getName();

    Group group = groupPort.findById(groupId).orElseThrow(GroupNotFoundException::new);

    validateLeaderAuthorization(userId, groupId);

    groupValidateUseCase.checkGroupNameDuplication(name);

    group.changeGroupName(name);

    groupPort.save(group);
  }


  private void validateLeaderAuthorization(String userId, String groupId) {
    if (!groupMemberPort.isLeaderByUserIdAndGroupId(userId, groupId)) {
      throw new UnauthorizedGroupAccessException();
    }
  }
}

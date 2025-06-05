package com.ssafy.witch.group;

import com.ssafy.witch.exception.group.AlreadyJoinedGroupException;
import com.ssafy.witch.exception.group.GroupJoinRequestExistsException;
import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.exception.user.UserNotFoundException;
import com.ssafy.witch.group.command.GroupJoinRequestCreateCommand;
import com.ssafy.witch.group.event.CreateGroupJoinRequestEvent;
import com.ssafy.witch.user.User;
import com.ssafy.witch.user.UserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GroupJoinRequestService implements CreateGroupJoinRequestUseCase {

  private final GroupPort groupPort;
  private final GroupMemberPort groupMemberPort;
  private final GroupJoinRequestPort groupJoinRequestPort;
  private final GroupReadPort groupReadPort;
  private final GroupEventPublishPort groupEventPublishPort;
  private final UserPort userPort;

  @Transactional
  @Override
  public void creatGroupJoinRequest(GroupJoinRequestCreateCommand command) {
    String userId = command.getUserId();
    String groupId = command.getGroupId();
    String joinRequestUserId = command.getUserId();

    validateGroupExists(groupId);
    validateUserNotParticipants(joinRequestUserId, groupId);
    validateGroupJoinRequestNotExists(joinRequestUserId, groupId);

    GroupJoinRequest groupJoinRequest = GroupJoinRequest.of(joinRequestUserId, groupId);
    groupJoinRequestPort.save(groupJoinRequest);

    User requestUser = userPort.findById(userId).orElseThrow(UserNotFoundException::new);
    GroupWithMemberUsers groupWithMemberUsers = groupReadPort.findGroupWithFcmTokenMember(groupId);
    groupEventPublishPort.publish(
        new CreateGroupJoinRequestEvent(requestUser, groupWithMemberUsers));

  }

  private void validateGroupJoinRequestNotExists(String userId, String groupId) {
    if (groupJoinRequestPort.existsByUserIdAndGroupId(userId, groupId)) {
      throw new GroupJoinRequestExistsException();
    }
  }

  private void validateUserNotParticipants(String userId, String groupId) {
    if (groupMemberPort.existsByUserIdAndGroupId(userId, groupId)) {
      throw new AlreadyJoinedGroupException();
    }
  }

  private void validateGroupExists(String groupId) {
    if (!groupPort.existsById(groupId)) {
      throw new GroupNotFoundException();
    }
  }

}

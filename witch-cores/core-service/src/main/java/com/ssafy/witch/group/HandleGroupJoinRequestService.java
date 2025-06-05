package com.ssafy.witch.group;

import com.ssafy.witch.exception.group.AlreadyJoinedGroupException;
import com.ssafy.witch.exception.group.GroupJoinRequestNotFoundException;
import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.group.command.ApproveGroupJoinRequestCommand;
import com.ssafy.witch.group.command.GetGroupJoinRequestListCommand;
import com.ssafy.witch.group.command.RejectGroupJoinRequestCommand;
import com.ssafy.witch.group.event.ApproveGroupJoinRequestEvent;
import com.ssafy.witch.group.event.RejectGroupJoinRequestEvent;
import com.ssafy.witch.group.mapper.GroupJoinRequestListOutputMapper;
import com.ssafy.witch.group.model.GroupJoinRequestProjection;
import com.ssafy.witch.group.output.GroupJoinRequestListOutput;
import com.ssafy.witch.user.User;
import com.ssafy.witch.user.UserPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class HandleGroupJoinRequestService implements HandleGroupJoinRequestUseCase {

  private final GroupJoinRequestPort groupJoinRequestPort;
  private final GroupMemberPort groupMemberPort;
  private final GroupPort groupPort;
  private final GroupJoinRequestListOutputMapper groupJoinRequestListOutputMapper;
  private final GroupEventPublishPort groupEventPublishPort;
  private final GroupReadPort groupReadPort;
  private final UserPort userPort;

  @Value("${witch.group.max-participants-count}")
  private int maxGroupParticipantsCount;

  @Transactional
  @Override
  public void approveGroupJoinRequest(ApproveGroupJoinRequestCommand command) {
    GroupJoinRequest groupJoinRequest = groupJoinRequestPort.findById(command.getJoinRequestId())
        .orElseThrow(GroupJoinRequestNotFoundException::new);

    String groupId = groupJoinRequest.getGroupId();
    String requestUserId = groupJoinRequest.getUserId();

    List<GroupMember> groupMembers = groupMemberPort.findAllByGroupId(groupId);

    validateUserNotParticipants(requestUserId, groupMembers);
    validateLeaderAuthorization(groupMembers, command.getUserId());
    validateGroupCapacity(groupMembers);

    groupJoinRequestPort.deleteById(command.getJoinRequestId());
    groupMemberPort.save(GroupMember.createNewGroupMember(groupJoinRequest.getUserId(), groupId));

    User joinMember = userPort.findById(requestUserId)
        .orElseThrow(GroupJoinRequestNotFoundException::new);
    GroupWithMemberUsers groupWithFcmTokenMembers = groupReadPort.findGroupWithFcmTokenMember(
        groupId);
    groupEventPublishPort.publish(
        new ApproveGroupJoinRequestEvent(joinMember, groupWithFcmTokenMembers));
  }

  @Transactional
  @Override
  public void rejectGroupJoinRequest(RejectGroupJoinRequestCommand command) {
    GroupJoinRequest groupJoinRequest = groupJoinRequestPort.findById(command.getJoinRequestId())
        .orElseThrow(GroupJoinRequestNotFoundException::new);

    String userId = command.getUserId();
    String groupId = groupJoinRequest.getGroupId();

    validateLeaderAuthorization(userId, groupId);

    groupJoinRequestPort.deleteById(command.getJoinRequestId());

    User joinMember = userPort.findById(userId)
        .orElseThrow(GroupJoinRequestNotFoundException::new);
    GroupWithMemberUsers groupWithFcmTokenMembers = groupReadPort.findGroupWithFcmTokenMember(
        groupId);
    groupEventPublishPort.publish(
        new RejectGroupJoinRequestEvent(joinMember, groupWithFcmTokenMembers));
  }

  private void validateLeaderAuthorization(List<GroupMember> groupMembers, String userId) {
    boolean isLeader = groupMembers.stream()
        .anyMatch(member -> member.getUserId().equals(userId) && member.getIsLeader());

    if (!isLeader) {
      throw new UnauthorizedGroupAccessException();
    }
  }

  private void validateLeaderAuthorization(String userId, String groupId) {
    if (!groupMemberPort.isLeaderByUserIdAndGroupId(userId, groupId)) {
      throw new UnauthorizedGroupAccessException();
    }
  }

  private void validateGroupCapacity(List<GroupMember> groupMembers) {
    if (groupMembers.size() >= maxGroupParticipantsCount) {
      throw new UnauthorizedGroupAccessException();
    }
  }

  private void validateUserNotParticipants(String requestUserId, List<GroupMember> groupMembers) {
    boolean isParticipant = groupMembers.stream()
        .anyMatch(member -> member.getUserId().equals(requestUserId));

    if (isParticipant) {
      throw new AlreadyJoinedGroupException();
    }
  }

  @Override
  public GroupJoinRequestListOutput getGroupJoinRequestList(
      GetGroupJoinRequestListCommand command) {

    String userId = command.getUserId();
    String groupId = command.getGroupId();

    //존재 하는 모임 확인
    validateExistsGroup(groupId);
    //권한 확인
    validateLeaderAuthorization(userId, groupId);

    //해당 모임에 대한 가입 신청 사용자 목록 조회
    List<GroupJoinRequestProjection> projections = groupJoinRequestPort.readGroupJoinRequestsByGroupId(
        groupId);

    return groupJoinRequestListOutputMapper.toOutput(projections);
  }

  private void validateExistsGroup(String groupId) {
    if (!groupPort.existsById(groupId)) {
      throw new GroupNotFoundException();
    }
  }
}

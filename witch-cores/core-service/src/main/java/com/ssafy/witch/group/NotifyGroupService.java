package com.ssafy.witch.group;

import com.ssafy.witch.group.command.NotifyGroupJoinRequestApproveCommand;
import com.ssafy.witch.group.command.NotifyGroupJoinRequestCommand;
import com.ssafy.witch.group.command.NotifyGroupJoinRequestRejectCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotifyGroupService implements NotifyGroupUseCase {

  private final NotifyGroupPort notifyGroupPort;

  @Override
  public void notifyJoinRequest(NotifyGroupJoinRequestCommand command) {
    String groupId = command.getGroupId();
    String groupName = command.getGroupName();
    String requestUserNickname = command.getRequestUserNickname();
    String targetUserFcmToken = command.getTargetUserFcmToken();

    JoinRequestCreateNotification notification = new JoinRequestCreateNotification(
        groupId, groupName, targetUserFcmToken, requestUserNickname);

    notifyGroupPort.notifyJoinRequestCreated(notification);
  }

  @Override
  public void notifyJoinRequestApproved(NotifyGroupJoinRequestApproveCommand command) {
    JoinRequestApprovedNotification notification = new JoinRequestApprovedNotification(
        command.getGroupId(),
        command.getGroupName(),
        command.getTargetUserFcmTokens(),
        command.getJoinUserNickname(),
        command.getJoinUserFcmToken()
    );

    notifyGroupPort.notifyJoinRequestApproved(notification);
  }

  @Override
  public void notifyJoinRequestReject(NotifyGroupJoinRequestRejectCommand command) {
    JoinRequestRejectedNotification notification = new JoinRequestRejectedNotification(
        command.getGroupId(),
        command.getGroupName(),
        command.getTargetUserFcmTokens(),
        command.getJoinUserNickname(),
        command.getJoinUserFcmToken()
    );

    notifyGroupPort.notifyJoinRequestRejected(notification);
  }
}

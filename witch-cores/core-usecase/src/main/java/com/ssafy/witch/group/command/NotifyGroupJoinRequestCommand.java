package com.ssafy.witch.group.command;

import com.ssafy.witch.group.Group;
import com.ssafy.witch.group.GroupMemberUser;
import com.ssafy.witch.group.GroupWithMemberUsers;
import com.ssafy.witch.group.event.CreateGroupJoinRequestEvent;
import java.util.List;
import lombok.Getter;

@Getter
public class NotifyGroupJoinRequestCommand {

  private final String groupId;
  private final String groupName;
  private final String targetUserFcmToken;
  private final String requestUserNickname;

  public NotifyGroupJoinRequestCommand(CreateGroupJoinRequestEvent event) {
    GroupWithMemberUsers groupWithMemberUsers = event.getGroupWithMemberUsers();

    Group group = groupWithMemberUsers.getGroup();
    List<GroupMemberUser> groupMemberUsers = groupWithMemberUsers.getGroupMemberUsers();

    this.groupId = group.getGroupId();
    this.groupName = group.getName();
    this.targetUserFcmToken = extractTargetUserFcmToken(groupMemberUsers);
    this.requestUserNickname = event.getJoinRequestUser().getNickname();

  }

  private String extractTargetUserFcmToken(List<GroupMemberUser> groupMemberUsers) {
    for (GroupMemberUser groupMemberUser : groupMemberUsers) {
      if (groupMemberUser.getGroupMember().getIsLeader()) {
        return groupMemberUser.getUserWithFcmToken().getFcmToken();
      }
    }
    return null;
  }
}

package com.ssafy.witch.group.command;

import com.ssafy.witch.group.Group;
import com.ssafy.witch.group.GroupMemberUser;
import com.ssafy.witch.group.GroupWithMemberUsers;
import com.ssafy.witch.group.event.RejectGroupJoinRequestEvent;
import com.ssafy.witch.user.User;
import java.util.List;
import lombok.Getter;

@Getter
public class NotifyGroupJoinRequestRejectCommand {

  private final String groupId;
  private final String groupName;
  private final List<String> targetUserFcmTokens;
  private final String joinUserNickname;
  private final String joinUserFcmToken;

  public NotifyGroupJoinRequestRejectCommand(RejectGroupJoinRequestEvent event) {
    GroupWithMemberUsers groupWithMemberUsers = event.getGroupWithMemberUsers();

    Group group = groupWithMemberUsers.getGroup();
    List<GroupMemberUser> groupMemberUsers = groupWithMemberUsers.getGroupMemberUsers();

    User joinUser = event.getJoinRequestUser();

    this.groupId = group.getGroupId();
    this.groupName = group.getName();
    this.targetUserFcmTokens = extractFcmTokens(groupMemberUsers, joinUser);
    this.joinUserNickname = event.getJoinRequestUser().getNickname();
    this.joinUserFcmToken = extractJoinUserFcmTokens(groupMemberUsers, joinUser);

  }

  private String extractJoinUserFcmTokens(List<GroupMemberUser> groupMemberUsers, User joinUser) {
    return groupMemberUsers.stream()
        .filter(member -> member.getUserWithFcmToken().getUser().getUserId()
            .equals(joinUser.getUserId()))
        .map(member -> member.getUserWithFcmToken().getFcmToken())
        .findAny()
        .orElseThrow();
  }

  private List<String> extractFcmTokens(List<GroupMemberUser> groupMemberUsers, User joinUser) {
    return groupMemberUsers.stream()
        .filter(member -> member.getUserWithFcmToken().getUser().getUserId()
            .equals(joinUser.getUserId()))
        .map(member -> member.getUserWithFcmToken().getFcmToken())
        .toList();
  }
}

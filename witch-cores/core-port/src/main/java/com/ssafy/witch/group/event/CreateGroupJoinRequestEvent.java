package com.ssafy.witch.group.event;

import com.ssafy.witch.group.GroupWithMemberUsers;
import com.ssafy.witch.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class CreateGroupJoinRequestEvent {

  private User joinRequestUser;
  private GroupWithMemberUsers groupWithMemberUsers;
}

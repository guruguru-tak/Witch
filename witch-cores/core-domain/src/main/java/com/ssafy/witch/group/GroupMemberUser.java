package com.ssafy.witch.group;

import com.ssafy.witch.user.UserWithFcmToken;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class GroupMemberUser {

  private GroupMember groupMember;
  private UserWithFcmToken userWithFcmToken;

}

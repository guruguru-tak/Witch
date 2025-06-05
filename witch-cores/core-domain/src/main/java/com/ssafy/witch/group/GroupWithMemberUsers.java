package com.ssafy.witch.group;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class GroupWithMemberUsers {

  private Group group;
  private List<GroupMemberUser> groupMemberUsers;

}

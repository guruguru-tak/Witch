package com.ssafy.witch.group.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupMemberListQuery {

  private final String userId;
  private final String groupId;

}

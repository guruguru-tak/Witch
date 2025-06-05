package com.ssafy.witch.group.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupMemberProjection {

  private String userId;
  private String nickname;
  private String profileImageUrl;
  private boolean isLeader;

}

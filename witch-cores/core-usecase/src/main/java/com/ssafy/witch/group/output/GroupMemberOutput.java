package com.ssafy.witch.group.output;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupMemberOutput {

  private String userId;
  private String nickname;
  private String profileImageUrl;
  private boolean isLeader;

}

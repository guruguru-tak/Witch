package com.ssafy.witch.controller.group.response;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class GroupMemberResponse {

  private String userId;
  private String nickname;
  private String profileImageUrl;
  private boolean isLeader;

}

package com.ssafy.witch.group.model;

import com.ssafy.witch.user.model.UserBasicProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupJoinRequestProjection {

  private String joinRequestId;
  private UserBasicProjection user;
}
